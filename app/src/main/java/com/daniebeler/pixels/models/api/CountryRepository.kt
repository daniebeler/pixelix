package com.daniebeler.pixels.models.api

interface CountryRepository {
    suspend fun searchCountries(): List<Post>
}