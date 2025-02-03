package com.daniebeler.pfpixelix.data.remote.dto

import com.daniebeler.pfpixelix.domain.model.Place
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlaceDto(
    @SerialName("id") val id: String,
    @SerialName("slug") val slug: String?,
    @SerialName("url") val url: String?,
    @SerialName("name") val name: String?,
    @SerialName("country") val country: String?
) : DtoInterface<Place> {
    override fun toModel(): Place {
        return Place(
            id = id, slug = slug, name = name, country = country, url = url
        )
    }
}