package com.daniebeler.pixelix.di

import com.daniebeler.pixelix.domain.repository.AccountRepository
import com.daniebeler.pixelix.domain.repository.CountryRepository
import com.daniebeler.pixelix.domain.repository.PostEditorRepository
import com.daniebeler.pixelix.domain.repository.PostRepository
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