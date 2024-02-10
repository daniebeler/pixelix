package com.daniebeler.pixelix.di

import com.daniebeler.pixelix.domain.repository.CountryRepository
import com.daniebeler.pixelix.domain.repository.StorageRepository
import com.daniebeler.pixelix.domain.usecase.BlockAccountUseCase
import com.daniebeler.pixelix.domain.usecase.FollowAccountUseCase
import com.daniebeler.pixelix.domain.usecase.GetAccount
import com.daniebeler.pixelix.domain.usecase.GetMutualFollowersUseCase
import com.daniebeler.pixelix.domain.usecase.GetOwnAccount
import com.daniebeler.pixelix.domain.usecase.GetOwnPosts
import com.daniebeler.pixelix.domain.usecase.GetPostsOfAccount
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
    fun provideGetAccountUseCase(repository: CountryRepository): GetAccount = GetAccount(repository)

    @Provides
    @Singleton
    fun provideGetOwnAccountUseCase(repository: CountryRepository): GetOwnAccount =
        GetOwnAccount(repository)


    @Provides
    @Singleton
    fun provideGetOwnPostsUseCase(repository: CountryRepository): GetOwnPosts =
        GetOwnPosts(repository)

    @Provides
    @Singleton
    fun provideGetPostsOfAccountUseCase(
        repository: CountryRepository, storageRepository: StorageRepository
    ): GetPostsOfAccount = GetPostsOfAccount(repository, storageRepository)

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
}