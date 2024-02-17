package com.daniebeler.pixelix.di

import HostSelectionInterceptor
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.daniebeler.pixelix.data.remote.PixelfedApi
import com.daniebeler.pixelix.data.repository.AccountRepositoryImpl
import com.daniebeler.pixelix.data.repository.CountryRepositoryImpl
import com.daniebeler.pixelix.data.repository.StorageRepositoryImpl
import com.daniebeler.pixelix.data.repository.TimelineRepositoryImpl
import com.daniebeler.pixelix.domain.repository.AccountRepository
import com.daniebeler.pixelix.domain.repository.CountryRepository
import com.daniebeler.pixelix.domain.repository.StorageRepository
import com.daniebeler.pixelix.domain.repository.TimelineRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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
    fun provideTimelineRepository(
        pixelfedApi: PixelfedApi
    ): TimelineRepository = TimelineRepositoryImpl(pixelfedApi)

    @Provides
    @Singleton
    fun provideAccountRepository(
        pixelfedApi: PixelfedApi
    ): AccountRepository = AccountRepositoryImpl(pixelfedApi)

    @Provides
    @Singleton
    fun provideHostSelectionInterceptor(): HostSelectionInterceptorInterface =
        HostSelectionInterceptor()

    @Provides
    @Singleton
    fun provideApiRepository(
        dataStore: DataStore<Preferences>,
        hostSelectionInterceptor: HostSelectionInterceptorInterface,
        pixelfedApi: PixelfedApi
    ): CountryRepository = CountryRepositoryImpl(dataStore, hostSelectionInterceptor, pixelfedApi)


    @Provides
    @Singleton
    fun provideOKHttpClient(hostSelectionInterceptor: HostSelectionInterceptorInterface): OkHttpClient {

        var loggi = HttpLoggingInterceptor()
        loggi.setLevel(HttpLoggingInterceptor.Level.BODY)

        return OkHttpClient.Builder().addInterceptor(hostSelectionInterceptor).addInterceptor(loggi)
            .build()
    }


    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit = Retrofit.Builder().addConverterFactory(
        GsonConverterFactory.create()
    ).client(client).baseUrl("https://pixelfed.fief/").build()


    @Provides
    @Singleton
    fun providePixelfedApi(retrofit: Retrofit): PixelfedApi =
        retrofit.create(PixelfedApi::class.java)

}