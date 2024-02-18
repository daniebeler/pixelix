package com.daniebeler.pfpixelix.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.daniebeler.pfpixelix.data.remote.PixelfedApi
import com.daniebeler.pfpixelix.data.repository.AccountRepositoryImpl
import com.daniebeler.pfpixelix.data.repository.CountryRepositoryImpl
import com.daniebeler.pfpixelix.data.repository.HashtagRepositoryImpl
import com.daniebeler.pfpixelix.data.repository.PostEditorRepositoryImpl
import com.daniebeler.pfpixelix.data.repository.PostRepositoryImpl
import com.daniebeler.pfpixelix.data.repository.StorageRepositoryImpl
import com.daniebeler.pfpixelix.data.repository.TimelineRepositoryImpl
import com.daniebeler.pfpixelix.domain.repository.AccountRepository
import com.daniebeler.pfpixelix.domain.repository.CountryRepository
import com.daniebeler.pfpixelix.domain.repository.HashtagRepository
import com.daniebeler.pfpixelix.domain.repository.PostEditorRepository
import com.daniebeler.pfpixelix.domain.repository.PostRepository
import com.daniebeler.pfpixelix.domain.repository.StorageRepository
import com.daniebeler.pfpixelix.domain.repository.TimelineRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

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
    fun provideApiRepository(
        dataStore: DataStore<Preferences>,
        hostSelectionInterceptor: HostSelectionInterceptorInterface,
        pixelfedApi: PixelfedApi
    ): CountryRepository = CountryRepositoryImpl(dataStore, hostSelectionInterceptor, pixelfedApi)
}