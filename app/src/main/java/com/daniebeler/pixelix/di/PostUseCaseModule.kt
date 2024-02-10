package com.daniebeler.pixelix.di

import com.daniebeler.pixelix.domain.repository.CountryRepository
import com.daniebeler.pixelix.domain.usecase.BookmarkPostUseCase
import com.daniebeler.pixelix.domain.usecase.CreatePostUseCase
import com.daniebeler.pixelix.domain.usecase.CreateReplyUseCase
import com.daniebeler.pixelix.domain.usecase.DeletePostUseCase
import com.daniebeler.pixelix.domain.usecase.GetAccountsWhoLikedPostUseCase
import com.daniebeler.pixelix.domain.usecase.GetLikedPostsUseCase
import com.daniebeler.pixelix.domain.usecase.GetPostUseCase
import com.daniebeler.pixelix.domain.usecase.GetRepliesUseCase
import com.daniebeler.pixelix.domain.usecase.LikePostUseCase
import com.daniebeler.pixelix.domain.usecase.UnbookmarkPostUseCase
import com.daniebeler.pixelix.domain.usecase.UnlikePostUseCase
import com.daniebeler.pixelix.domain.usecase.UpdateMediaUseCase
import com.daniebeler.pixelix.domain.usecase.UploadMediaUseCase
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

    @Provides
    @Singleton
    fun provideBookmarkPostUseCase(repository: CountryRepository): BookmarkPostUseCase =
        BookmarkPostUseCase(repository)

    @Provides
    @Singleton
    fun provideUnbookmarkPostUseCase(repository: CountryRepository): UnbookmarkPostUseCase =
        UnbookmarkPostUseCase(repository)


    @Provides
    @Singleton
    fun provideLikePostUseCase(repository: CountryRepository): LikePostUseCase =
        LikePostUseCase(repository)

    @Provides
    @Singleton
    fun provideUnlikePostUseCase(repository: CountryRepository): UnlikePostUseCase =
        UnlikePostUseCase(repository)

    @Provides
    @Singleton
    fun provideGetAccountsWhoLikedPostUseCase(repository: CountryRepository): GetAccountsWhoLikedPostUseCase =
        GetAccountsWhoLikedPostUseCase(repository)

    @Provides
    @Singleton
    fun provideGetRepliesUseCase(repository: CountryRepository): GetRepliesUseCase =
        GetRepliesUseCase(repository)

    @Provides
    @Singleton
    fun provideCreateReplyUseCase(repository: CountryRepository): CreateReplyUseCase =
        CreateReplyUseCase(repository)

    @Provides
    @Singleton
    fun provideDeletePostUseCase(repository: CountryRepository): DeletePostUseCase =
        DeletePostUseCase(repository)

    @Provides
    @Singleton
    fun provideCreatePostUseCase(repository: CountryRepository): CreatePostUseCase =
        CreatePostUseCase(repository)

    @Provides
    @Singleton
    fun provideUploadMediaUseCase(repository: CountryRepository): UploadMediaUseCase =
        UploadMediaUseCase(repository)

    @Provides
    @Singleton
    fun provideUpdateMediaUseCase(repository: CountryRepository): UpdateMediaUseCase =
        UpdateMediaUseCase(repository)
}