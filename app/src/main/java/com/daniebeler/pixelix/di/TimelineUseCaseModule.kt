package com.daniebeler.pixelix.di

import com.daniebeler.pixelix.domain.repository.CountryRepository
import com.daniebeler.pixelix.domain.repository.StorageRepository
import com.daniebeler.pixelix.domain.usecase.GetGlobalTimeline
import com.daniebeler.pixelix.domain.usecase.GetHashtagTimeline
import com.daniebeler.pixelix.domain.usecase.GetHomeTimeline
import com.daniebeler.pixelix.domain.usecase.GetLocalTimeline
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
    ): GetHomeTimeline = GetHomeTimeline(repository, storageRepository)

    @Provides
    @Singleton
    fun provideGetLocalTimelineUseCase(
        repository: CountryRepository, storageRepository: StorageRepository
    ): GetLocalTimeline = GetLocalTimeline(repository, storageRepository)

    @Provides
    @Singleton
    fun provideGetGlobalTimelineUseCase(
        repository: CountryRepository, storageRepository: StorageRepository
    ): GetGlobalTimeline = GetGlobalTimeline(repository, storageRepository)

    @Provides
    @Singleton
    fun provideGetHashtagTimelineUseCase(
        repository: CountryRepository, storageRepository: StorageRepository
    ): GetHashtagTimeline = GetHashtagTimeline(repository, storageRepository)
}