package com.daniebeler.pixels.models.api

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object Module {

    @Provides
    fun provideCountryRepository(): CountryRepository {
        return CountryRepositoryImpl()
    }
}