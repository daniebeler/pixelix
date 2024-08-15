package com.daniebeler.pfpixelix.di

import com.daniebeler.pfpixelix.domain.repository.AccountRepository
import com.daniebeler.pfpixelix.domain.repository.CountryRepository
import com.daniebeler.pfpixelix.domain.repository.PostRepository
import com.daniebeler.pfpixelix.domain.repository.StorageRepository
import com.daniebeler.pfpixelix.domain.usecase.BlockAccountUseCase
import com.daniebeler.pfpixelix.domain.usecase.FollowAccountUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetAccountByUsernameUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetAccountUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetAccountsFollowersUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetAccountsFollowingUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetCurrentLoginDataUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetMutualFollowersUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetOwnAccountUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetOwnPostsUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetPostsOfAccountUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetRelationshipsUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetViewUseCase
import com.daniebeler.pfpixelix.domain.usecase.MuteAccountUseCase
import com.daniebeler.pfpixelix.domain.usecase.SetViewUseCase
import com.daniebeler.pfpixelix.domain.usecase.UnblockAccountUseCase
import com.daniebeler.pfpixelix.domain.usecase.UnfollowAccountUseCase
import com.daniebeler.pfpixelix.domain.usecase.UnmuteAccountUseCase
import com.daniebeler.pfpixelix.domain.usecase.UpdateAccountUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AccountUseCaseModule {

    @Provides
    @Singleton
    fun provideGetAccountUseCase(accountRepository: AccountRepository): GetAccountUseCase =
        GetAccountUseCase(accountRepository)

    @Provides
    @Singleton
    fun provideGetAccountByUsernameUseCase(accountRepository: AccountRepository): GetAccountByUsernameUseCase =
        GetAccountByUsernameUseCase(accountRepository)

    @Provides
    @Singleton
    fun provideGetOwnAccountUseCase(
        currentLoginDataUseCase: GetCurrentLoginDataUseCase, accountRepository: AccountRepository
    ): GetOwnAccountUseCase = GetOwnAccountUseCase(currentLoginDataUseCase, accountRepository)


    @Provides
    @Singleton
    fun provideGetOwnPostsUseCase(
        postRepository: PostRepository, currentLoginDataUseCase: GetCurrentLoginDataUseCase
    ): GetOwnPostsUseCase = GetOwnPostsUseCase(postRepository, currentLoginDataUseCase)

    @Provides
    @Singleton
    fun provideGetPostsOfAccountUseCase(
        postRepository: PostRepository, storageRepository: StorageRepository
    ): GetPostsOfAccountUseCase = GetPostsOfAccountUseCase(postRepository, storageRepository)

    @Provides
    @Singleton
    fun provideFollowAccountUseCase(accountRepository: AccountRepository): FollowAccountUseCase =
        FollowAccountUseCase(accountRepository)

    @Provides
    @Singleton
    fun provideUnfollowAccountUseCase(accountRepository: AccountRepository): UnfollowAccountUseCase =
        UnfollowAccountUseCase(accountRepository)

    @Provides
    @Singleton
    fun provideMuteAccountUseCase(accountRepository: AccountRepository): MuteAccountUseCase =
        MuteAccountUseCase(accountRepository)

    @Provides
    @Singleton
    fun provideUnmuteAccountUseCase(accountRepository: AccountRepository): UnmuteAccountUseCase =
        UnmuteAccountUseCase(accountRepository)

    @Provides
    @Singleton
    fun provideBlockAccountUseCase(accountRepository: AccountRepository): BlockAccountUseCase =
        BlockAccountUseCase(accountRepository)

    @Provides
    @Singleton
    fun provideUnblockAccountUseCase(accountRepository: AccountRepository): UnblockAccountUseCase =
        UnblockAccountUseCase(accountRepository)

    @Provides
    @Singleton
    fun provideGetRelationshipsUseCase(repository: CountryRepository): GetRelationshipsUseCase =
        GetRelationshipsUseCase(repository)

    @Provides
    @Singleton
    fun provideGetMutualFollowersUseCase(accountRepository: AccountRepository): GetMutualFollowersUseCase =
        GetMutualFollowersUseCase(accountRepository)

    @Provides
    @Singleton
    fun provideGetAccountsFollowersUseCase(accountRepository: AccountRepository): GetAccountsFollowersUseCase =
        GetAccountsFollowersUseCase(accountRepository)

    @Provides
    @Singleton
    fun provideGetAccountsFollowingUseCase(accountRepository: AccountRepository): GetAccountsFollowingUseCase =
        GetAccountsFollowingUseCase(accountRepository)

    @Provides
    @Singleton
    fun provideUpdateAccountUseCase(repository: AccountRepository): UpdateAccountUseCase =
        UpdateAccountUseCase(repository)

    @Provides
    @Singleton
    fun provideGetViewUseCase(repository: StorageRepository): GetViewUseCase =
        GetViewUseCase(repository)

    @Provides
    @Singleton
    fun provideSetViewUseCase(repository: StorageRepository): SetViewUseCase =
        SetViewUseCase(repository)
}