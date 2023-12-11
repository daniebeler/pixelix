package com.daniebeler.pixels.models.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory

class CountryRepositoryImpl: CountryRepository {

    private val BASE_URL = "https://restcountries.com/v3.1/"

    private val countryApi: CountryApi

    init {
        val mHttpLoggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)

        val mOkHttpClient = OkHttpClient
            .Builder()
            .addInterceptor(mHttpLoggingInterceptor)
            .build()

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(mOkHttpClient)
            .build()

        countryApi = retrofit.create(CountryApi::class.java)
    }

    override suspend fun searchCountries(query: String): List<Country> {
        return try {
            val response = countryApi.searchCountries(query).awaitResponse()
            if (response.isSuccessful) {
                val countries = response.body() ?: emptyList()
                countries.map { it.toModel() }
            } else {
                emptyList()
            }
        } catch (exception: Exception) {
            val e = exception
            emptyList()
        }
    }
}