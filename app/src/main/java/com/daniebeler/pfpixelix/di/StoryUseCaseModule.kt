package com.daniebeler.pfpixelix.di

import com.daniebeler.pfpixelix.domain.repository.StoryRepository
import com.daniebeler.pfpixelix.domain.usecase.GetStoriesCarouselUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class StoryUseCaseModule {

    @Provides
    @Singleton
    fun provideGetStoriesCarouselUseCase(repository: StoryRepository): GetStoriesCarouselUseCase =
        GetStoriesCarouselUseCase(repository)
}