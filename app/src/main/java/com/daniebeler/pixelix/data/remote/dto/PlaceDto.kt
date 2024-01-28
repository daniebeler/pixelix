package com.daniebeler.pixelix.data.remote.dto

import com.daniebeler.pixelix.domain.model.Place
import com.google.gson.annotations.SerializedName

data class PlaceDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("slug")
    val slug: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("country")
    val country: String
) : DtoInterface<Place> {
    override fun toModel(): Place {
        return Place(
            id = id,
            slug = slug,
            name = name,
            country = country
        )
    }
}