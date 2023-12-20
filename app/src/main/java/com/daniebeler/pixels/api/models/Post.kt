package com.daniebeler.pixels.api.models

import com.google.gson.annotations.SerializedName

data class Post(
    @SerializedName("id")
    val id: String,
    @SerializedName("media_attachments")
    val mediaAttachments: List<MediaAttachment>,
    @SerializedName("account")
    val account: Account,
    @SerializedName("tags")
    val tags: List<Hashtag>,
    @SerializedName("favourites_count")
    val likes: Int,
    @SerializedName("content_text")
    val content: String,
    @SerializedName("reply_count")
    val replyCount: Int
)

data class MediaAttachment(
    @SerializedName("id")
    val id: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("preview_url")
    val previewUrl: String
)