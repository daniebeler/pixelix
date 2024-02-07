package com.daniebeler.pixelix.di

import com.daniebeler.pixelix.domain.repository.CountryRepository
import com.daniebeler.pixelix.domain.repository.StorageRepository
import com.daniebeler.pixelix.domain.usecase.GetHideSensitiveContent
import com.daniebeler.pixelix.domain.usecase.GetOwnInstanceDomain
import com.daniebeler.pixelix.domain.usecase.Logout
import com.daniebeler.pixelix.domain.usecase.StoreHideSensitiveContent
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
    fun provideStoreHideSensitiveContentUseCase(repository: StorageRepository): StoreHideSensitiveContent =
        StoreHideSensitiveContent(repository)

    @Provides
    @Singleton
    fun provideGetHideSensitiveContentUseCase(repository: StorageRepository): GetHideSensitiveContent =
        GetHideSensitiveContent(repository)

    @Provides
    @Singleton
    fun provideLogoutUseCase(repository: CountryRepository): Logout = Logout(repository)

    @Provides
    @Singleton
    fun provideGetOwnInstanceDomainUseCase(repository: StorageRepository): GetOwnInstanceDomain =
        GetOwnInstanceDomain(repository)
}