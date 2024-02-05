package com.daniebeler.pixelix.di

import com.daniebeler.pixelix.domain.repository.CountryRepository
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
    fun provideGetHomeTimelineUseCase(repository: CountryRepository): GetHomeTimeline =
        GetHomeTimeline(repository)

    @Provides
    @Singleton
    fun provideGetLocalTimelineUseCase(repository: CountryRepository): GetLocalTimeline =
        GetLocalTimeline(repository)

    @Provides
    @Singleton
    fun provideGetGlobalTimelineUseCase(repository: CountryRepository): GetGlobalTimeline =
        GetGlobalTimeline(repository)

    @Provides
    @Singleton
    fun provideGetHashtagTimelineUseCase(repository: CountryRepository): GetHashtagTimeline =
        GetHashtagTimeline(repository)
}