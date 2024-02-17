package com.daniebeler.pixelix.di

import com.daniebeler.pixelix.domain.repository.AccountRepository
import com.daniebeler.pixelix.domain.repository.CountryRepository
import com.daniebeler.pixelix.domain.repository.PostRepository
import com.daniebeler.pixelix.domain.repository.StorageRepository
import com.daniebeler.pixelix.domain.usecase.GetBlockedAccountsUseCase
import com.daniebeler.pixelix.domain.usecase.GetBookmarkedPostsUseCase
import com.daniebeler.pixelix.domain.usecase.GetHideSensitiveContentUseCase
import com.daniebeler.pixelix.domain.usecase.GetInstanceUseCase
import com.daniebeler.pixelix.domain.usecase.GetMutedAccountsUseCase
import com.daniebeler.pixelix.domain.usecase.GetOwnInstanceDomainUseCase
import com.daniebeler.pixelix.domain.usecase.LogoutUseCase
import com.daniebeler.pixelix.domain.usecase.StoreHideSensitiveContentUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SettingsUseCaseModule {

    @Provides
    @Singleton
    fun provideStoreHideSensitiveContentUseCase(repository: StorageRepository): StoreHideSensitiveContentUseCase =
        StoreHideSensitiveContentUseCase(repository)

    @Provides
    @Singleton
    fun provideGetHideSensitiveContentUseCase(repository: StorageRepository): GetHideSensitiveContentUseCase =
        GetHideSensitiveContentUseCase(repository)

    @Provides
    @Singleton
    fun provideLogoutUseCase(
        repository: CountryRepository, storageRepository: StorageRepository
    ): LogoutUseCase = LogoutUseCase(repository, storageRepository)

    @Provides
    @Singleton
    fun provideGetBookmarkedPostsUseCase(postRepository: PostRepository): GetBookmarkedPostsUseCase =
        GetBookmarkedPostsUseCase(postRepository)

    @Provides
    @Singleton
    fun provideGetMutedAccountsUseCase(accountRepository: AccountRepository): GetMutedAccountsUseCase =
        GetMutedAccountsUseCase(accountRepository)

    @Provides
    @Singleton
    fun provideGetBlockedAccountsUseCase(accountRepository: AccountRepository): GetBlockedAccountsUseCase =
        GetBlockedAccountsUseCase(accountRepository)

    @Provides
    @Singleton
    fun provideGetInstanceUseCase(repository: CountryRepository): GetInstanceUseCase =
        GetInstanceUseCase(repository)

    @Provides
    @Singleton
    fun provideGetOwnInstanceDomainUseCase(repository: StorageRepository): GetOwnInstanceDomainUseCase =
        GetOwnInstanceDomainUseCase(repository)
}