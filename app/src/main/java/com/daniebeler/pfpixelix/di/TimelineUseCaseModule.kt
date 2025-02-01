package com.daniebeler.pfpixelix.di

import com.daniebeler.pfpixelix.domain.repository.StorageRepository
import com.daniebeler.pfpixelix.domain.repository.TimelineRepository
import com.daniebeler.pfpixelix.domain.usecase.GetGlobalTimelineUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetHashtagTimelineUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetHomeTimelineUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetLocalTimelineUseCase
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
    ): GetHomeTimelineUseCase = GetHomeTimelineUseCase(timelineRepository)

    @Provides
    @Singleton
    fun provideGetLocalTimelineUseCase(
        timelineRepository: TimelineRepository, storageRepository: StorageRepository
    ): GetLocalTimelineUseCase = GetLocalTimelineUseCase(timelineRepository)

    @Provides
    @Singleton
    fun provideGetGlobalTimelineUseCase(
        timelineRepository: TimelineRepository, storageRepository: StorageRepository
    ): GetGlobalTimelineUseCase = GetGlobalTimelineUseCase(timelineRepository)

    @Provides
    @Singleton
    fun provideGetHashtagTimelineUseCase(
        timelineRepository: TimelineRepository, storageRepository: StorageRepository
    ): GetHashtagTimelineUseCase = GetHashtagTimelineUseCase(timelineRepository)
}