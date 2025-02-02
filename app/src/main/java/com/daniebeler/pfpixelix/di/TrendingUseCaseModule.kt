package com.daniebeler.pfpixelix.di

import android.content.Context
import com.daniebeler.pfpixelix.domain.repository.CountryRepository
import com.daniebeler.pfpixelix.domain.repository.HashtagRepository
import com.daniebeler.pfpixelix.domain.repository.PostRepository
import com.daniebeler.pfpixelix.domain.usecase.GetTrendingAccountsUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetTrendingHashtagsUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetTrendingPostsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class TrendingUseCaseModule {

    @Provides
    @Singleton
    fun provideGetTrendingPostsUseCase(
        postRepository: PostRepository, @ApplicationContext context: Context
    ): GetTrendingPostsUseCase = GetTrendingPostsUseCase(postRepository, context)

    @Provides
    @Singleton
    fun provideGetTrendingAccountsUseCase(repository: CountryRepository): GetTrendingAccountsUseCase =
        GetTrendingAccountsUseCase(repository)

    @Provides
    @Singleton
    fun provideGetTrendingHashtagsUseCase(hashtagRepository: HashtagRepository): GetTrendingHashtagsUseCase =
        GetTrendingHashtagsUseCase(hashtagRepository)
}