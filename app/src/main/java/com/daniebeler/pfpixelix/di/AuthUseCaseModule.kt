package com.daniebeler.pfpixelix.di

import com.daniebeler.pfpixelix.domain.repository.CountryRepository
import com.daniebeler.pfpixelix.domain.repository.StorageRepository
import com.daniebeler.pfpixelix.domain.usecase.GetClientIdUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetClientSecretUseCase
import com.daniebeler.pfpixelix.domain.usecase.ObtainTokenUseCase
import com.daniebeler.pfpixelix.domain.usecase.StoreAccessTokenUseCase
import com.daniebeler.pfpixelix.domain.usecase.StoreAccountIdUseCase
import com.daniebeler.pfpixelix.domain.usecase.VerifyTokenUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AuthUseCaseModule {

    @Provides
    @Singleton
    fun provideObtainTokenUseCase(repository: CountryRepository): ObtainTokenUseCase =
        ObtainTokenUseCase(repository)

    @Provides
    @Singleton
    fun provideGetClientSecretUseCase(storageRepository: StorageRepository): GetClientSecretUseCase =
        GetClientSecretUseCase(storageRepository)

    @Provides
    @Singleton
    fun provideGetClientIdUseCase(storageRepository: StorageRepository): GetClientIdUseCase =
        GetClientIdUseCase(storageRepository)

    @Provides
    @Singleton
    fun provideStoreAccessTokenUseCase(repository: CountryRepository): StoreAccessTokenUseCase =
        StoreAccessTokenUseCase(repository)

    @Provides
    @Singleton
    fun provideStoreAccountIdUseCase(storageRepository: StorageRepository): StoreAccountIdUseCase =
        StoreAccountIdUseCase(storageRepository)

    @Provides
    @Singleton
    fun provideVerifyTokenUseCase(repository: CountryRepository): VerifyTokenUseCase =
        VerifyTokenUseCase(repository)
}