package com.daniebeler.pixelix.di

import com.daniebeler.pixelix.domain.repository.StorageRepository
import com.daniebeler.pixelix.domain.repository.TimelineRepository
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
        timelineRepository: TimelineRepository, storageRepository: StorageRepository
    ): GetHomeTimelineUseCase = GetHomeTimelineUseCase(timelineRepository, storageRepository)

    @Provides
    @Singleton
    fun provideGetLocalTimelineUseCase(
        timelineRepository: TimelineRepository, storageRepository: StorageRepository
    ): GetLocalTimelineUseCase = GetLocalTimelineUseCase(timelineRepository, storageRepository)

    @Provides
    @Singleton
    fun provideGetGlobalTimelineUseCase(
        timelineRepository: TimelineRepository, storageRepository: StorageRepository
    ): GetGlobalTimelineUseCase = GetGlobalTimelineUseCase(timelineRepository, storageRepository)

    @Provides
    @Singleton
    fun provideGetHashtagTimelineUseCase(
        timelineRepository: TimelineRepository, storageRepository: StorageRepository
    ): GetHashtagTimelineUseCase = GetHashtagTimelineUseCase(timelineRepository, storageRepository)
}