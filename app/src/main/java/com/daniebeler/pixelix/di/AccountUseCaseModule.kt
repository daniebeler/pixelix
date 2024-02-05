package com.daniebeler.pixelix.di

import com.daniebeler.pixelix.domain.repository.CountryRepository
import com.daniebeler.pixelix.domain.usecase.GetAccount
import com.daniebeler.pixelix.domain.usecase.GetOwnAccount
import com.daniebeler.pixelix.domain.usecase.GetOwnPosts
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AccountUseCaseModule {

    @Provides
    @Singleton
    fun provideGetAccountUseCase(repository: CountryRepository): GetAccount = GetAccount(repository)

    @Provides
    @Singleton
    fun provideGetOwnAccountUseCase(repository: CountryRepository): GetOwnAccount =
        GetOwnAccount(repository)


    @Provides
    @Singleton
    fun provideGetOwnPostsUseCase(repository: CountryRepository): GetOwnPosts =
        GetOwnPosts(repository)
}