package com.daniebeler.pixelix.di

import com.daniebeler.pixelix.domain.repository.AccountRepository
import com.daniebeler.pixelix.domain.repository.CountryRepository
import com.daniebeler.pixelix.domain.repository.PostRepository
import com.daniebeler.pixelix.domain.repository.StorageRepository
import com.daniebeler.pixelix.domain.usecase.BlockAccountUseCase
import com.daniebeler.pixelix.domain.usecase.FollowAccountUseCase
import com.daniebeler.pixelix.domain.usecase.GetAccountUseCase
import com.daniebeler.pixelix.domain.usecase.GetAccountsFollowersUseCase
import com.daniebeler.pixelix.domain.usecase.GetAccountsFollowingUseCase
import com.daniebeler.pixelix.domain.usecase.GetMutualFollowersUseCase
import com.daniebeler.pixelix.domain.usecase.GetOwnAccountIdUseCase
import com.daniebeler.pixelix.domain.usecase.GetOwnAccountUseCase
import com.daniebeler.pixelix.domain.usecase.GetOwnPostsUseCase
import com.daniebeler.pixelix.domain.usecase.GetPostsOfAccountUseCase
import com.daniebeler.pixelix.domain.usecase.GetRelationshipsUseCase
import com.daniebeler.pixelix.domain.usecase.MuteAccountUseCase
import com.daniebeler.pixelix.domain.usecase.UnblockAccountUseCase
import com.daniebeler.pixelix.domain.usecase.UnfollowAccountUseCase
import com.daniebeler.pixelix.domain.usecase.UnmuteAccountUseCase
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
    fun provideGetOwnAccountUseCase(
        storageRepository: StorageRepository, accountRepository: AccountRepository
    ): GetOwnAccountUseCase = GetOwnAccountUseCase(storageRepository, accountRepository)


    @Provides
    @Singleton
    fun provideGetOwnPostsUseCase(
        postRepository: PostRepository, storageRepository: StorageRepository
    ): GetOwnPostsUseCase = GetOwnPostsUseCase(postRepository, storageRepository)

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
    fun provideGetMutualFollowersUseCase(repository: CountryRepository): GetMutualFollowersUseCase =
        GetMutualFollowersUseCase(repository)

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
    fun provideGetOwnAccountIdUseCase(repository: StorageRepository): GetOwnAccountIdUseCase =
        GetOwnAccountIdUseCase(repository)
}