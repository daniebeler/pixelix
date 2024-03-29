package com.daniebeler.pfpixelix.di

import com.daniebeler.pfpixelix.domain.repository.CountryRepository
import com.daniebeler.pfpixelix.domain.repository.HashtagRepository
import com.daniebeler.pfpixelix.domain.repository.PostRepository
import com.daniebeler.pfpixelix.domain.repository.StorageRepository
import com.daniebeler.pfpixelix.domain.usecase.GetDomainSoftwareUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetTrendingAccountsUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetTrendingHashtagsUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetTrendingPostsUseCase
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
    ): GetDomainSoftwareUseCase = GetDomainSoftwareUseCase(countryRepository)
}