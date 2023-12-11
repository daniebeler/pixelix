package com.daniebeler.pixels.models.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface CountryApi {
    @GET("name/{countryName}")
    fun searchCountries(@Path("countryName") countryName: String): Call<List<CountryDTO>>
}