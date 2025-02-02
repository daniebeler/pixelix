package com.daniebeler.pfpixelix.data.remote.dto

import com.daniebeler.pfpixelix.domain.model.Collection
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CollectionDto(
    @SerialName("avatar") val avatar: String?,
    @SerialName("description") val description: String?,
    @SerialName("id") val id: String,
    @SerialName("pid") val pid: String?,
    @SerialName("post_count") val postCount: Int,
    @SerialName("published_at") val publishedAt: String?,
    @SerialName("thumb") val thumb: String,
    @SerialName("title") val title: String?,
    @SerialName("updated_at") val updatedAt: String?,
    @SerialName("url") val url: String,
    @SerialName("username") val username: String,
    @SerialName("visibility") val visibility: String
) : DtoInterface<Collection> {
    override fun toModel(): Collection {
        return Collection(
            id = id,
            visibility = visibility,
            title = title ?: "",
            description = description ?:"",
            thumbnail = thumb,
            postCount = postCount,
            username = username,
            url = url
        )
    }
}