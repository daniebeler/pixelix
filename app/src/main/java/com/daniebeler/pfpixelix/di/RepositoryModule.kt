package com.daniebeler.pfpixelix.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.Preferences
import com.daniebeler.pfpixelix.data.remote.PixelfedApi
import com.daniebeler.pfpixelix.data.repository.AccountRepositoryImpl
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
import com.daniebeler.pfpixelix.domain.model.SavedSearches
import com.daniebeler.pfpixelix.domain.repository.AccountRepository
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
import com.daniebeler.pfpixelix.utils.SavedSearchesSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    @Provides
    @Singleton
    fun savedSearchesRepository(
        dataStore: DataStore<SavedSearches>
    ): SavedSearchesRepository = SavedSearchesRepositoryImpl(dataStore)

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<SavedSearches> =
        DataStoreFactory.create(serializer = SavedSearchesSerializer(),
            produceFile = { context.dataStoreFile("saved_searches.json") })

    @Provides
    @Singleton
    fun provideStorageRepository(
        dataStore: DataStore<Preferences>
    ): StorageRepository = StorageRepositoryImpl(dataStore)

    @Provides
    @Singleton
    fun provideTimelineRepository(
        pixelfedApi: PixelfedApi
    ): TimelineRepository = TimelineRepositoryImpl(pixelfedApi)

    @Provides
    @Singleton
    fun provideAccountRepository(
        pixelfedApi: PixelfedApi
    ): AccountRepository = AccountRepositoryImpl(pixelfedApi)

    @Provides
    @Singleton
    fun provideCollectionRepository(
        pixelfedApi: PixelfedApi
    ): CollectionRepository = CollectionRepositoryImpl(pixelfedApi)

    @Provides
    @Singleton
    fun provideDirectMessagesRepository(
        pixelfedApi: PixelfedApi
    ): DirectMessagesRepository = DirectMessagesRepositoryImpl(pixelfedApi)

    @Provides
    @Singleton
    fun provideHashtagRepository(
        pixelfedApi: PixelfedApi
    ): HashtagRepository = HashtagRepositoryImpl(pixelfedApi)

    @Provides
    @Singleton
    fun providePostRepository(
        pixelfedApi: PixelfedApi
    ): PostRepository = PostRepositoryImpl(pixelfedApi)

    @Provides
    @Singleton
    fun providePostEditorRepository(
        pixelfedApi: PixelfedApi
    ): PostEditorRepository = PostEditorRepositoryImpl(pixelfedApi)

    @Provides
    @Singleton
    fun provideWidgetRepository(
        pixelfedApi: PixelfedApi
    ): WidgetRepository = WidgetRepositoryImpl(pixelfedApi)


    @Provides
    @Singleton
    fun provideApiRepository(
        dataStore: DataStore<Preferences>,
        hostSelectionInterceptor: HostSelectionInterceptorInterface,
        pixelfedApi: PixelfedApi
    ): CountryRepository = CountryRepositoryImpl(dataStore, hostSelectionInterceptor, pixelfedApi)
}