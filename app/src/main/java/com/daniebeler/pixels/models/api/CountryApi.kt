package com.daniebeler.pixels.models.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface CountryApi {
    @GET("v2/discover/posts/trending")
    fun searchCountries(): Call<List<CountryDTO>>
}