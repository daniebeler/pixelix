package com.daniebeler.pfpixelix.di

import HostSelectionInterceptor
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import co.touchlab.kermit.Logger
import com.daniebeler.pfpixelix.data.remote.PixelfedApi
import com.daniebeler.pfpixelix.data.remote.createPixelfedApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.converter.CallConverterFactory
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.plugin
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import javax.inject.Singleton


private val Context.dataStore by preferencesDataStore("settings")

@InstallIn(SingletonComponent::class)
@Module
class Module {


    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        explicitNulls = false
    }

    @Provides
    @Singleton
    fun provideUserDataStorePreferences(
        @ApplicationContext applicationContext: Context
    ): DataStore<Preferences> {
        return applicationContext.dataStore
    }


    @Provides
    @Singleton
    fun provideHostSelectionInterceptor(): HostSelectionInterceptorInterface =
        HostSelectionInterceptor()


    @Provides
    @Singleton
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
    @Singleton
    fun provideKtorfit(client: HttpClient): Ktorfit = Ktorfit.Builder()
        .converterFactories(CallConverterFactory())
        .httpClient(client)
        .baseUrl("https://err.or/")
        .build()


    @Provides
    @Singleton
    fun providePixelfedApi(ktorfit: Ktorfit): PixelfedApi =
        ktorfit.createPixelfedApi()
}