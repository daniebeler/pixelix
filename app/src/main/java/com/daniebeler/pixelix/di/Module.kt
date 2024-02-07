package com.daniebeler.pixelix.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.daniebeler.pixelix.data.repository.CountryRepositoryImpl
import com.daniebeler.pixelix.data.repository.StorageRepositoryImpl
import com.daniebeler.pixelix.domain.repository.CountryRepository
import com.daniebeler.pixelix.domain.repository.StorageRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore("settings")

@InstallIn(SingletonComponent::class)
@Module
class Module {

    @Provides
    @Singleton
    fun provideUserDataStorePreferences(
        @ApplicationContext applicationContext: Context
    ): DataStore<Preferences> {
        return applicationContext.dataStore
    }

    @Provides
    @Singleton
    fun provideStorageRepository(
        dataStore: DataStore<Preferences>
    ): StorageRepository = StorageRepositoryImpl(dataStore)


    @Provides
    @Singleton
    fun provideApiRepository(
        dataStore: DataStore<Preferences>
    ): CountryRepository = CountryRepositoryImpl(dataStore)


}