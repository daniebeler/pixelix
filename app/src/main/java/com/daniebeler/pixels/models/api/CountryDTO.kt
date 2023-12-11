package com.daniebeler.pixels.models.api

import com.google.gson.annotations.SerializedName

data class CountryDTO(
    @SerializedName("name")
    val name: Name,
    @SerializedName("region")
    val region: String,
    @SerializedName("area")
    val area: Double,
    @SerializedName("population")
    val population: Int
)

data class Name(
    @SerializedName("common")
    val common: String
)

fun CountryDTO.toModel() = Country(
    name = this.name.common,
    region = this.region,
    area = this.area,
    population = this.population
)