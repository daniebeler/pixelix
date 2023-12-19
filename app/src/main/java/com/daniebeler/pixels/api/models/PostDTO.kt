package com.daniebeler.pixels.api.models

import com.google.gson.annotations.SerializedName

data class PostDTO(
    @SerializedName("id")
    val id: String,
    @SerializedName("media_attachments")
    val mediaAttachments: List<MediaAttachment>,
    @SerializedName("account")
    val account: AccountDTO,
    @SerializedName("tags")
    val tags: List<Tag>,
    @SerializedName("liked_by")
    val liked_by: LikedBy,
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

data class AccountDTO(
    @SerializedName("id")
    val id: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("display_name")
    val displayname: String,
    @SerializedName("avatar")
    val avatar: String,
)

data class Tag(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)

data class LikedBy(
    @SerializedName("total_count")
    val total_count: Int
)

fun PostDTO.toModel() = Post(
    id = this.id,
    mediaAttachments = this.mediaAttachments,
    account = this.account,
    tags = this.tags,
    likes = this.liked_by.total_count,
    replyCount = this.replyCount,
    content = this.content
)