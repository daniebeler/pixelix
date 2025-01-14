package com.daniebeler.pfpixelix.di

import com.daniebeler.pfpixelix.domain.repository.CollectionRepository
import com.daniebeler.pfpixelix.domain.usecase.AddPostOfCollectionUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetCollectionUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetCollectionsUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetPostsOfCollectionUseCase
import com.daniebeler.pfpixelix.domain.usecase.RemovePostOfCollectionUseCase
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

    @Provides
    @Singleton
    fun provideGetCollectionUseCase(repository: CollectionRepository): GetCollectionUseCase =
        GetCollectionUseCase(repository)

    @Provides
    @Singleton
    fun provideGetPostsOfCollectionUseCase(repository: CollectionRepository): GetPostsOfCollectionUseCase =
        GetPostsOfCollectionUseCase(repository)

    @Provides
    @Singleton
    fun provideRemovePostsOfCollectionUseCase(repository: CollectionRepository): RemovePostOfCollectionUseCase =
        RemovePostOfCollectionUseCase(repository)

    @Provides
    @Singleton
    fun provideAddPostsOfCollectionUseCase(repository: CollectionRepository): AddPostOfCollectionUseCase =
        AddPostOfCollectionUseCase(repository)
}