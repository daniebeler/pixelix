package com.daniebeler.pfpixelix.di

import android.content.Context
import com.daniebeler.pfpixelix.domain.repository.TimelineRepository
import com.daniebeler.pfpixelix.domain.usecase.GetGlobalTimelineUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetHashtagTimelineUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetHomeTimelineUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetLocalTimelineUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class TimelineUseCaseModule {

    @Provides
    @Singleton
    fun provideGetHomeTimelineUseCase(
        timelineRepository: TimelineRepository, @ApplicationContext context: Context
    ): GetHomeTimelineUseCase = GetHomeTimelineUseCase(timelineRepository, context)

    @Provides
    @Singleton
    fun provideGetLocalTimelineUseCase(
        timelineRepository: TimelineRepository, @ApplicationContext context: Context
    ): GetLocalTimelineUseCase = GetLocalTimelineUseCase(timelineRepository, context)

    @Provides
    @Singleton
    fun provideGetGlobalTimelineUseCase(
        timelineRepository: TimelineRepository, @ApplicationContext context: Context
    ): GetGlobalTimelineUseCase = GetGlobalTimelineUseCase(timelineRepository, context)

    @Provides
    @Singleton
    fun provideGetHashtagTimelineUseCase(
        timelineRepository: TimelineRepository, @ApplicationContext context: Context
    ): GetHashtagTimelineUseCase = GetHashtagTimelineUseCase(timelineRepository, context)
}