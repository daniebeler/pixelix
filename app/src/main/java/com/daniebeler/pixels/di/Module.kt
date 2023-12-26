package com.daniebeler.pixels.di

import android.content.Context
import com.daniebeler.pixels.domain.repository.CountryRepository
import com.daniebeler.pixels.data.repository.CountryRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object Module {

    @Provides
    fun provideCountryRepository(@ApplicationContext context: Context): CountryRepository = CountryRepositoryImpl(context)
}