package com.daniebeler.pixels.models.api

import com.google.gson.annotations.SerializedName

data class CountryDTO(
    @SerializedName("id")
    val id: String,

    @SerializedName("media_attachments")
    val mediaAttachments: List<MediaAttachment>
)

data class MediaAttachment(
    @SerializedName("id")
    val id: String,
    val url: String
)

fun CountryDTO.toModel() = Post(
    id = this.id,
    mediaAttachments = this.mediaAttachments
)