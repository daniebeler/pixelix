package com.daniebeler.pixelix.di

import com.daniebeler.pixelix.domain.repository.CountryRepository
import com.daniebeler.pixelix.domain.usecase.GetLikedPostsUseCase
import com.daniebeler.pixelix.domain.usecase.GetPostUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class PostUseCaseModule {

    @Provides
    @Singleton
    fun provideGetPostUseCase(repository: CountryRepository): GetPostUseCase =
        GetPostUseCase(repository)

    @Provides
    @Singleton
    fun provideGetLikedPostsUseCase(repository: CountryRepository): GetLikedPostsUseCase =
        GetLikedPostsUseCase(repository)
}