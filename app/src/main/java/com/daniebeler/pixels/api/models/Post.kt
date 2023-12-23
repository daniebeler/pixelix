package com.daniebeler.pixels.api.models

import com.daniebeler.pixels.data.remote.dto.AccountDto
import com.daniebeler.pixels.domain.model.Account
import com.google.gson.annotations.SerializedName

data class Post(
    @SerializedName("id")
    val id: String,
    @SerializedName("media_attachments")
    val mediaAttachments: List<MediaAttachment>,
    @SerializedName("account")
    val account: AccountDto,
    @SerializedName("tags")
    val tags: List<Hashtag>,
    @SerializedName("favourites_count")
    val likes: Int,
    @SerializedName("content_text")
    val content: String,
    @SerializedName("reply_count")
    val replyCount: Int,
    @SerializedName("created_at")
    val createdAt: String
)

data class MediaAttachment(
    @SerializedName("id")
    val id: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("preview_url")
    val previewUrl: String,
    @SerializedName("meta")
    val meta: Meta?
)

data class Meta(
    @SerializedName("original")
    val original: OriginalDataClass?
)

data class OriginalDataClass(
    @SerializedName("aspect")
    val aspect: Float?
)