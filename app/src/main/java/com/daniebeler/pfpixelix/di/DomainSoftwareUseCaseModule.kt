package com.daniebeler.pfpixelix.di

import com.daniebeler.pfpixelix.domain.repository.CountryRepository
import com.daniebeler.pfpixelix.domain.usecase.nodeinfo.GetFediServerUseCase
import com.daniebeler.pfpixelix.domain.usecase.nodeinfo.GetFediSoftwareUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DomainSoftwareUseCaseModule {
    @Provides
    @Singleton
    fun provideGetDomainSoftwareUseCase(
        countryRepository: CountryRepository
    ): GetFediServerUseCase = GetFediServerUseCase(countryRepository)

    @Provides
    @Singleton
    fun provideGetFediSoftwareUseCase(
        countryRepository: CountryRepository
    ): GetFediSoftwareUseCase = GetFediSoftwareUseCase(countryRepository)
}