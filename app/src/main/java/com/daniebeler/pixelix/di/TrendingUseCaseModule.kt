package com.daniebeler.pixelix.di

import com.daniebeler.pixelix.domain.repository.CountryRepository
import com.daniebeler.pixelix.domain.usecase.GetTrendingPosts
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class TrendingUseCaseModule {

    @Provides
    @Singleton
    fun provideGetTrendingPostsUseCase(repository: CountryRepository): GetTrendingPosts = GetTrendingPosts(repository)
}