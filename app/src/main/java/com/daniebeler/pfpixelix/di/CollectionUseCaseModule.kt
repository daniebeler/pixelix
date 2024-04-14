package com.daniebeler.pfpixelix.di

import com.daniebeler.pfpixelix.domain.repository.CollectionRepository
import com.daniebeler.pfpixelix.domain.usecase.GetCollectionsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class CollectionUseCaseModule {

    @Provides
    @Singleton
    fun provideGetCollectionsUseCase(repository: CollectionRepository): GetCollectionsUseCase =
        GetCollectionsUseCase(repository)
}