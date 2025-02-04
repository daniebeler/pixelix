package com.daniebeler.pfpixelix.di

import HostSelectionInterceptor
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.okio.OkioStorage
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import co.touchlab.kermit.Logger
import com.daniebeler.pfpixelix.data.remote.PixelfedApi
import com.daniebeler.pfpixelix.data.remote.createPixelfedApi
import com.daniebeler.pfpixelix.data.repository.AccountRepositoryImpl
import com.daniebeler.pfpixelix.data.repository.AuthRepositoryImpl
import com.daniebeler.pfpixelix.data.repository.CollectionRepositoryImpl
import com.daniebeler.pfpixelix.data.repository.CountryRepositoryImpl
import com.daniebeler.pfpixelix.data.repository.DirectMessagesRepositoryImpl
import com.daniebeler.pfpixelix.data.repository.HashtagRepositoryImpl
import com.daniebeler.pfpixelix.data.repository.PostEditorRepositoryImpl
import com.daniebeler.pfpixelix.data.repository.PostRepositoryImpl
import com.daniebeler.pfpixelix.data.repository.SavedSearchesRepositoryImpl
import com.daniebeler.pfpixelix.data.repository.StorageRepositoryImpl
import com.daniebeler.pfpixelix.data.repository.TimelineRepositoryImpl
import com.daniebeler.pfpixelix.data.repository.WidgetRepositoryImpl
import com.daniebeler.pfpixelix.domain.model.AuthData
import com.daniebeler.pfpixelix.domain.model.SavedSearches
import com.daniebeler.pfpixelix.domain.repository.AccountRepository
import com.daniebeler.pfpixelix.domain.repository.AuthRepository
import com.daniebeler.pfpixelix.domain.repository.CollectionRepository
import com.daniebeler.pfpixelix.domain.repository.CountryRepository
import com.daniebeler.pfpixelix.domain.repository.DirectMessagesRepository
import com.daniebeler.pfpixelix.domain.repository.HashtagRepository
import com.daniebeler.pfpixelix.domain.repository.PostEditorRepository
import com.daniebeler.pfpixelix.domain.repository.PostRepository
import com.daniebeler.pfpixelix.domain.repository.SavedSearchesRepository
import com.daniebeler.pfpixelix.domain.repository.StorageRepository
import com.daniebeler.pfpixelix.domain.repository.TimelineRepository
import com.daniebeler.pfpixelix.domain.repository.WidgetRepository
import com.daniebeler.pfpixelix.utils.AuthDataSerializer
import com.daniebeler.pfpixelix.utils.KmpContext
import com.daniebeler.pfpixelix.utils.SavedSearchesSerializer
import com.daniebeler.pfpixelix.utils.dataStoreDir
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.converter.CallConverterFactory
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.plugin
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides
import me.tatarka.inject.annotations.Scope
import okio.FileSystem
import okio.SYSTEM

@Scope
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER)
annotation class AppSingleton

@AppSingleton
@Component
abstract class AppComponent(
    @get:Provides val context: KmpContext
) {
    
    @Provides
    @AppSingleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        explicitNulls = false
    }

    @Provides
    @AppSingleton
    fun provideHostSelectionInterceptor(): HostSelectionInterceptorInterface =
        HostSelectionInterceptor()


    @Provides
    @AppSingleton
    fun provideHttpClient(
        json: Json,
        hostSelectionInterceptor: HostSelectionInterceptorInterface
    ): HttpClient = HttpClient {
        install(ContentNegotiation) { json(json) }
        install(Logging) {
            logger = object : io.ktor.client.plugins.logging.Logger {
                override fun log(message: String) {
                    Logger.v("HttpClient") {
                        message.lines().joinToString { "\n\t\t$it"}
                    }
                }
            }
            level = LogLevel.BODY
        }
    }.apply {
        plugin(HttpSend).intercept { request ->
            with(hostSelectionInterceptor) {
                intercept(request)
            }
        }
    }


    @Provides
    @AppSingleton
    fun provideKtorfit(client: HttpClient): Ktorfit = Ktorfit.Builder()
        .converterFactories(CallConverterFactory())
        .httpClient(client)
        .baseUrl("https://err.or/")
        .build()


    @Provides
    @AppSingleton
    fun providePixelfedApi(ktorfit: Ktorfit): PixelfedApi =
        ktorfit.createPixelfedApi()

    @Provides
    @AppSingleton
    fun providePreferences(context: KmpContext): DataStore<Preferences> =
        PreferenceDataStoreFactory.createWithPath(
            corruptionHandler = null,
            migrations = emptyList(),
            produceFile = { context.dataStoreDir.resolve("settings.preferences_pb") },
        )

    @Provides
    @AppSingleton
    fun provideSavedSearchesDataStore(context: KmpContext): DataStore<SavedSearches> =
        DataStoreFactory.create(
            storage = OkioStorage(
                fileSystem = FileSystem.SYSTEM,
                producePath = { context.dataStoreDir.resolve("saved_searches.json") },
                serializer = SavedSearchesSerializer,
            )
        )

    @Provides
    @AppSingleton
    fun provideAuthDataDataStore(context: KmpContext): DataStore<AuthData> =
        DataStoreFactory.create(
            storage = OkioStorage(
                fileSystem = FileSystem.SYSTEM,
                producePath = { context.dataStoreDir.resolve("auth_data_datastore.json") },
                serializer = AuthDataSerializer,
            )
        )
    
    @Provides
    fun provideAccountRepository(impl: AccountRepositoryImpl): AccountRepository = impl
    @Provides
    fun provideAuthRepository(impl: AuthRepositoryImpl): AuthRepository = impl
    @Provides
    fun provideCollectionRepository(impl: CollectionRepositoryImpl): CollectionRepository = impl
    @Provides
    fun provideCountryRepository(impl: CountryRepositoryImpl): CountryRepository = impl
    @Provides
    fun provideDirectMessagesRepository(impl: DirectMessagesRepositoryImpl): DirectMessagesRepository = impl
    @Provides
    fun provideHashtagRepository(impl: HashtagRepositoryImpl): HashtagRepository = impl
    @Provides
    fun providePostEditorRepository(impl: PostEditorRepositoryImpl): PostEditorRepository = impl
    @Provides
    fun providePostRepository(impl: PostRepositoryImpl): PostRepository = impl
    @Provides
    fun provideSavedSearchesRepository(impl: SavedSearchesRepositoryImpl): SavedSearchesRepository = impl
    @Provides
    fun provideStorageRepository(impl: StorageRepositoryImpl): StorageRepository = impl
    @Provides
    fun provideTimelineRepository(impl: TimelineRepositoryImpl): TimelineRepository = impl
    @Provides
    fun provideWidgetRepository(impl: WidgetRepositoryImpl): WidgetRepository = impl
}