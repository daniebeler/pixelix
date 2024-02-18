package com.daniebeler.pfpixelix.di

import com.daniebeler.pfpixelix.domain.repository.AccountRepository
import com.daniebeler.pfpixelix.domain.repository.CountryRepository
import com.daniebeler.pfpixelix.domain.repository.PostEditorRepository
import com.daniebeler.pfpixelix.domain.repository.PostRepository
import com.daniebeler.pfpixelix.domain.usecase.BookmarkPostUseCase
import com.daniebeler.pfpixelix.domain.usecase.CreatePostUseCase
import com.daniebeler.pfpixelix.domain.usecase.CreateReplyUseCase
import com.daniebeler.pfpixelix.domain.usecase.DeletePostUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetAccountsWhoLikedPostUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetLikedPostsUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetPostUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetRepliesUseCase
import com.daniebeler.pfpixelix.domain.usecase.LikePostUseCase
import com.daniebeler.pfpixelix.domain.usecase.UnbookmarkPostUseCase
import com.daniebeler.pfpixelix.domain.usecase.UnlikePostUseCase
import com.daniebeler.pfpixelix.domain.usecase.UpdateMediaUseCase
import com.daniebeler.pfpixelix.domain.usecase.UploadMediaUseCase
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
    fun provideGetPostUseCase(postRepository: PostRepository): GetPostUseCase =
        GetPostUseCase(postRepository)

    @Provides
    @Singleton
    fun provideGetLikedPostsUseCase(postRepository: PostRepository): GetLikedPostsUseCase =
        GetLikedPostsUseCase(postRepository)

    @Provides
    @Singleton
    fun provideBookmarkPostUseCase(postRepository: PostRepository): BookmarkPostUseCase =
        BookmarkPostUseCase(postRepository)

    @Provides
    @Singleton
    fun provideUnbookmarkPostUseCase(postRepository: PostRepository): UnbookmarkPostUseCase =
        UnbookmarkPostUseCase(postRepository)


    @Provides
    @Singleton
    fun provideLikePostUseCase(postRepository: PostRepository): LikePostUseCase =
        LikePostUseCase(postRepository)

    @Provides
    @Singleton
    fun provideUnlikePostUseCase(postRepository: PostRepository): UnlikePostUseCase =
        UnlikePostUseCase(postRepository)

    @Provides
    @Singleton
    fun provideGetAccountsWhoLikedPostUseCase(accountRepository: AccountRepository): GetAccountsWhoLikedPostUseCase =
        GetAccountsWhoLikedPostUseCase(accountRepository)

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
    fun provideDeletePostUseCase(postEditorRepository: PostEditorRepository): DeletePostUseCase =
        DeletePostUseCase(postEditorRepository)

    @Provides
    @Singleton
    fun provideCreatePostUseCase(postEditorRepository: PostEditorRepository): CreatePostUseCase =
        CreatePostUseCase(postEditorRepository)

    @Provides
    @Singleton
    fun provideUploadMediaUseCase(postEditorRepository: PostEditorRepository): UploadMediaUseCase =
        UploadMediaUseCase(postEditorRepository)

    @Provides
    @Singleton
    fun provideUpdateMediaUseCase(postEditorRepository: PostEditorRepository): UpdateMediaUseCase =
        UpdateMediaUseCase(postEditorRepository)
}