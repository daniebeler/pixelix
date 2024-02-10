package com.daniebeler.pixelix.di

import com.daniebeler.pixelix.domain.repository.CountryRepository
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
    fun provideGetAccountUseCase(repository: CountryRepository): GetAccountUseCase =
        GetAccountUseCase(repository)

    @Provides
    @Singleton
    fun provideGetOwnAccountUseCase(repository: CountryRepository): GetOwnAccountUseCase =
        GetOwnAccountUseCase(repository)


    @Provides
    @Singleton
    fun provideGetOwnPostsUseCase(repository: CountryRepository): GetOwnPostsUseCase =
        GetOwnPostsUseCase(repository)

    @Provides
    @Singleton
    fun provideGetPostsOfAccountUseCase(
        repository: CountryRepository, storageRepository: StorageRepository
    ): GetPostsOfAccountUseCase = GetPostsOfAccountUseCase(repository, storageRepository)

    @Provides
    @Singleton
    fun provideFollowAccountUseCase(repository: CountryRepository): FollowAccountUseCase =
        FollowAccountUseCase(repository)

    @Provides
    @Singleton
    fun provideUnfollowAccountUseCase(repository: CountryRepository): UnfollowAccountUseCase =
        UnfollowAccountUseCase(repository)

    @Provides
    @Singleton
    fun provideMuteAccountUseCase(repository: CountryRepository): MuteAccountUseCase =
        MuteAccountUseCase(repository)

    @Provides
    @Singleton
    fun provideUnmuteAccountUseCase(repository: CountryRepository): UnmuteAccountUseCase =
        UnmuteAccountUseCase(repository)

    @Provides
    @Singleton
    fun provideBlockAccountUseCase(repository: CountryRepository): BlockAccountUseCase =
        BlockAccountUseCase(repository)

    @Provides
    @Singleton
    fun provideUnblockAccountUseCase(repository: CountryRepository): UnblockAccountUseCase =
        UnblockAccountUseCase(repository)

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
    fun provideGetAccountsFollowersUseCase(repository: CountryRepository): GetAccountsFollowersUseCase =
        GetAccountsFollowersUseCase(repository)

    @Provides
    @Singleton
    fun provideGetAccountsFollowingUseCase(repository: CountryRepository): GetAccountsFollowingUseCase =
        GetAccountsFollowingUseCase(repository)

    @Provides
    @Singleton
    fun provideGetOwnAccountIdUseCase(repository: StorageRepository): GetOwnAccountIdUseCase =
        GetOwnAccountIdUseCase(repository)
}