package com.daniebeler.pfpixelix.di

import android.content.Context
import com.daniebeler.pfpixelix.domain.repository.AccountRepository
import com.daniebeler.pfpixelix.domain.repository.AuthRepository
import com.daniebeler.pfpixelix.domain.repository.CountryRepository
import com.daniebeler.pfpixelix.domain.repository.PostRepository
import com.daniebeler.pfpixelix.domain.repository.SavedSearchesRepository
import com.daniebeler.pfpixelix.domain.repository.StorageRepository
import com.daniebeler.pfpixelix.domain.usecase.GetActiveAppIconUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetBlockedAccountsUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetBookmarkedPostsUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetHideAltTextButtonUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetInstanceUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetMutedAccountsUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetOwnInstanceDomainUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetUseInAppBrowserUseCase
import com.daniebeler.pfpixelix.domain.usecase.LogoutUseCase
import com.daniebeler.pfpixelix.domain.usecase.OpenExternalUrlUseCase
import com.daniebeler.pfpixelix.domain.usecase.StoreThemeUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SettingsUseCaseModule {

    @Provides
    @Singleton
    fun provideGetHideAltTextButtonUseCase(repository: StorageRepository): GetHideAltTextButtonUseCase =
        GetHideAltTextButtonUseCase(repository)

    @Provides
    @Singleton
    fun provideLogoutUseCase(
        repository: AuthRepository, savedSearchesRepository: SavedSearchesRepository
    ): LogoutUseCase = LogoutUseCase(repository, savedSearchesRepository)

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
    fun provideGetOwnInstanceDomainUseCase(repository: AuthRepository): GetOwnInstanceDomainUseCase =
        GetOwnInstanceDomainUseCase(repository)

    @Provides
    @Singleton
    fun provideOpenExternalUrlUseCase(@ApplicationContext context: Context): OpenExternalUrlUseCase =
        OpenExternalUrlUseCase(context)

    @Provides
    @Singleton
    fun provideGetUseInAppBrowserUseCase(repository: StorageRepository): GetUseInAppBrowserUseCase =
        GetUseInAppBrowserUseCase(repository)

    @Provides
    @Singleton
    fun provideStoreThemeUseCase(repository: StorageRepository): StoreThemeUseCase =
        StoreThemeUseCase(repository)

    @Provides
    @Singleton
    fun provideGetAppIconUseCase(): GetActiveAppIconUseCase = GetActiveAppIconUseCase()
}