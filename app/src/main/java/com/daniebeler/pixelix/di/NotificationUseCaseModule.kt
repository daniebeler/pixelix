package com.daniebeler.pixelix.di

import com.daniebeler.pixelix.domain.repository.CountryRepository
import com.daniebeler.pixelix.domain.usecase.GetNotificationsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class NotificationUseCaseModule {

    @Provides
    @Singleton
    fun provideGetNotificationsUseCase(repository: CountryRepository): GetNotificationsUseCase =
        GetNotificationsUseCase(repository)
}