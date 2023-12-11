package com.daniebeler.pixels.models.api

import com.google.gson.annotations.SerializedName

data class CountryDTO(
    @SerializedName("id")
    val id: String,

    @SerializedName("media_attachments")
    val mediaAttachments: ArrayList<MediaAttachment>
)

data class MediaAttachment(
    @SerializedName("id")
    val id: String
)

fun CountryDTO.toModel() = Post(
    id = this.id,
    mediaAttachments = this.mediaAttachments
)