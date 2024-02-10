package com.daniebeler.pixelix.di

import com.daniebeler.pixelix.domain.repository.CountryRepository
import com.daniebeler.pixelix.domain.usecase.FollowHashtagUseCase
import com.daniebeler.pixelix.domain.usecase.GetFollowedHashtagsUseCase
import com.daniebeler.pixelix.domain.usecase.GetHashtagUseCase
import com.daniebeler.pixelix.domain.usecase.UnfollowHashtagUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class HashtagModule {

    @Provides
    @Singleton
    fun provideGetHashtagUseCase(repository: CountryRepository): GetHashtagUseCase =
        GetHashtagUseCase(repository)

    @Provides
    @Singleton
    fun provideFollowHashtagUseCase(repository: CountryRepository): FollowHashtagUseCase =
        FollowHashtagUseCase(repository)

    @Provides
    @Singleton
    fun provideUnfollowHashtagUseCase(repository: CountryRepository): UnfollowHashtagUseCase =
        UnfollowHashtagUseCase(repository)

    @Provides
    @Singleton
    fun provideGetFollowedHashtagsUseCase(repository: CountryRepository): GetFollowedHashtagsUseCase =
        GetFollowedHashtagsUseCase(repository)
}