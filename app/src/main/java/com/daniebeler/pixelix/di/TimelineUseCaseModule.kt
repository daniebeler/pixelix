package com.daniebeler.pixelix.di

import com.daniebeler.pixelix.domain.repository.CountryRepository
import com.daniebeler.pixelix.domain.repository.StorageRepository
import com.daniebeler.pixelix.domain.usecase.GetGlobalTimelineUseCase
import com.daniebeler.pixelix.domain.usecase.GetHashtagTimelineUseCase
import com.daniebeler.pixelix.domain.usecase.GetHomeTimelineUseCase
import com.daniebeler.pixelix.domain.usecase.GetLocalTimelineUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class TimelineUseCaseModule {

    @Provides
    @Singleton
    fun provideGetHomeTimelineUseCase(
        repository: CountryRepository, storageRepository: StorageRepository
    ): GetHomeTimelineUseCase = GetHomeTimelineUseCase(repository, storageRepository)

    @Provides
    @Singleton
    fun provideGetLocalTimelineUseCase(
        repository: CountryRepository, storageRepository: StorageRepository
    ): GetLocalTimelineUseCase = GetLocalTimelineUseCase(repository, storageRepository)

    @Provides
    @Singleton
    fun provideGetGlobalTimelineUseCase(
        repository: CountryRepository, storageRepository: StorageRepository
    ): GetGlobalTimelineUseCase = GetGlobalTimelineUseCase(repository, storageRepository)

    @Provides
    @Singleton
    fun provideGetHashtagTimelineUseCase(
        repository: CountryRepository, storageRepository: StorageRepository
    ): GetHashtagTimelineUseCase = GetHashtagTimelineUseCase(repository, storageRepository)
}