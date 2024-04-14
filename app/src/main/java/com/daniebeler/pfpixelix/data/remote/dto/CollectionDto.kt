package com.daniebeler.pfpixelix.data.remote.dto

import com.daniebeler.pfpixelix.domain.model.Collection
import com.google.gson.annotations.SerializedName

data class CollectionDto(
    @SerializedName("avatar") val avatar: String,
    @SerializedName("description") val description: String,
    @SerializedName("id") val id: String,
    @SerializedName("pid") val pid: String,
    @SerializedName("post_count") val postCount: Int,
    @SerializedName("published_at") val publishedAt: String,
    @SerializedName("thumb") val thumb: String,
    @SerializedName("title") val title: String,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("url") val url: String,
    @SerializedName("username") val username: String,
    @SerializedName("visibility") val visibility: String
) : DtoInterface<Collection> {
    override fun toModel(): Collection {
        return Collection(
            id = id,
            visibility = visibility,
            title = title,
            description = description,
            thumbnail = thumb,
            postCount = postCount,
            username = username,
            url = url
        )
    }
}