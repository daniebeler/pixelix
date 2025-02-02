package com.daniebeler.pfpixelix.di

import HostSelectionInterceptor
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.daniebeler.pfpixelix.data.remote.PixelfedApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton


private val Context.dataStore by preferencesDataStore("settings")

@InstallIn(SingletonComponent::class)
@Module
class Module {


    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        explicitNulls = false
    }

    @Provides
    @Singleton
    fun provideUserDataStorePreferences(
        @ApplicationContext applicationContext: Context
    ): DataStore<Preferences> {
        return applicationContext.dataStore
    }


    @Provides
    @Singleton
    fun provideHostSelectionInterceptor(): HostSelectionInterceptorInterface =
        HostSelectionInterceptor()


    @Provides
    @Singleton
    fun provideOKHttpClient(hostSelectionInterceptor: HostSelectionInterceptorInterface): OkHttpClient {

        val loggi = HttpLoggingInterceptor()
        loggi.setLevel(HttpLoggingInterceptor.Level.BODY)

        return OkHttpClient.Builder().addInterceptor(hostSelectionInterceptor).addInterceptor(loggi).addInterceptor(HtmlEntityDecodingInterceptor())
            .build()
    }


    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient, json: Json): Retrofit = Retrofit.Builder().addConverterFactory(
        json.asConverterFactory("application/json; charset=UTF8".toMediaType())
    ).client(client).baseUrl("https://err.or/").build()


    @Provides
    @Singleton
    fun providePixelfedApi(retrofit: Retrofit): PixelfedApi =
        retrofit.create(PixelfedApi::class.java)
}