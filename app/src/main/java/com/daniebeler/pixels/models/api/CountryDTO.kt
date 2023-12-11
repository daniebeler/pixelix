package com.daniebeler.pixels.models.api

import com.google.gson.annotations.SerializedName

data class CountryDTO(
    @SerializedName("id")
    val id: String,

    @SerializedName("media_attachments")
    val mediaAttachments: List<MediaAttachment>,

    @SerializedName("account")
    val account: Account
)

data class MediaAttachment(
    @SerializedName("id")
    val id: String,
    @SerializedName("url")
    val url: String
)

data class Account(
    @SerializedName("id")
    val id: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("display_name")
    val displayname: String,
    @SerializedName("avatar")
    val avatar: String,
)

fun CountryDTO.toModel() = Post(
    id = this.id,
    mediaAttachments = this.mediaAttachments,
    account = this.account
)