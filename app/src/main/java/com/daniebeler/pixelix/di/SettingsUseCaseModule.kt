package com.daniebeler.pixelix.di

import com.daniebeler.pixelix.domain.repository.StorageRepository
import com.daniebeler.pixelix.domain.usecase.GetHideSensitiveContent
import com.daniebeler.pixelix.domain.usecase.StoreHideSensitiveContent
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SettingsUseCaseModule {

    @Provides
    @Singleton
    fun provideStoreHideSensitiveContentUseCase(repository: StorageRepository): StoreHideSensitiveContent =
        StoreHideSensitiveContent(repository)

    @Provides
    @Singleton
    fun provideGetHideSensitiveContentUseCase(repository: StorageRepository): GetHideSensitiveContent =
        GetHideSensitiveContent(repository)
}