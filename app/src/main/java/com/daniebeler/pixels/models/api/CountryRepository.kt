package com.daniebeler.pixels.models.api

interface CountryRepository {
    suspend fun searchCountries(query: String): List<Country>
}