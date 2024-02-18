package com.daniebeler.pfpixelix.di

import com.daniebeler.pfpixelix.domain.repository.HashtagRepository
import com.daniebeler.pfpixelix.domain.usecase.FollowHashtagUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetFollowedHashtagsUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetHashtagUseCase
import com.daniebeler.pfpixelix.domain.usecase.UnfollowHashtagUseCase
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
    fun provideGetHashtagUseCase(hashtagRepository: HashtagRepository): GetHashtagUseCase =
        GetHashtagUseCase(hashtagRepository)

    @Provides
    @Singleton
    fun provideFollowHashtagUseCase(hashtagRepository: HashtagRepository): FollowHashtagUseCase =
        FollowHashtagUseCase(hashtagRepository)

    @Provides
    @Singleton
    fun provideUnfollowHashtagUseCase(hashtagRepository: HashtagRepository): UnfollowHashtagUseCase =
        UnfollowHashtagUseCase(hashtagRepository)

    @Provides
    @Singleton
    fun provideGetFollowedHashtagsUseCase(hashtagRepository: HashtagRepository): GetFollowedHashtagsUseCase =
        GetFollowedHashtagsUseCase(hashtagRepository)
}