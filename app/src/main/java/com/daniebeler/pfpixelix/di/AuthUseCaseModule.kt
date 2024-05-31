package com.daniebeler.pfpixelix.di

import com.daniebeler.pfpixelix.domain.repository.AuthRepository
import com.daniebeler.pfpixelix.domain.repository.CountryRepository
import com.daniebeler.pfpixelix.domain.usecase.AddNewLoginUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetAuthDataUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetCurrentLoginDataUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetOngoingLoginUseCase
import com.daniebeler.pfpixelix.domain.usecase.ObtainTokenUseCase
import com.daniebeler.pfpixelix.domain.usecase.UpdateCurrentUserUseCase
import com.daniebeler.pfpixelix.domain.usecase.UpdateLoginDataUseCase
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
    fun provideVerifyTokenUseCase(repository: CountryRepository): VerifyTokenUseCase =
        VerifyTokenUseCase(repository)

    @Provides
    @Singleton
    fun provideAddInitialLoginUseCase(
        repository: AuthRepository,
        hostSelectionInterceptorInterface: HostSelectionInterceptorInterface
    ): AddNewLoginUseCase =
        AddNewLoginUseCase(repository, hostSelectionInterceptorInterface)

    @Provides
    @Singleton
    fun provideFinishLoginUseCase(
        repository: AuthRepository,
        hostSelectionInterceptorInterface: HostSelectionInterceptorInterface
    ): UpdateLoginDataUseCase =
        UpdateLoginDataUseCase(repository, hostSelectionInterceptorInterface)

    @Provides
    @Singleton
    fun provideGetOngoingLoginUseCase(
        repository: AuthRepository,
    ): GetOngoingLoginUseCase =
        GetOngoingLoginUseCase(repository)

    @Provides
    @Singleton
    fun provideGetCurrentLoginDataUseCase(
        repository: AuthRepository,
    ): GetCurrentLoginDataUseCase =
        GetCurrentLoginDataUseCase(repository)

    @Provides
    @Singleton
    fun provideGetAuthDataUseCase(
        repository: AuthRepository,
    ): GetAuthDataUseCase =
        GetAuthDataUseCase(repository)

    @Provides
    @Singleton
    fun provideUpdateCurrentUserUseCase(
        repository: AuthRepository,
        hostSelectionInterceptorInterface: HostSelectionInterceptorInterface
    ): UpdateCurrentUserUseCase =
        UpdateCurrentUserUseCase(repository, hostSelectionInterceptorInterface)
}