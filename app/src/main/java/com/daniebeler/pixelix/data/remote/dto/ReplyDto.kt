package com.daniebeler.pixelix.data.remote.dto


import com.google.gson.annotations.SerializedName

data class ReplyDto(
    @SerializedName("account")
    val account: AccountDto,
    @SerializedName("application")
    val application: ApplicationDto,
    @SerializedName("comments_disabled")
    val commentsDisabled: Boolean,
    @SerializedName("content")
    val content: String,
    @SerializedName("content_text")
    val contentText: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("edited_at")
    val editedAt: String,
    @SerializedName("emojis")
    val emojis: List<Any>,
    @SerializedName("favourited")
    val favourited: Boolean,
    @SerializedName("favourites_count")
    val favouritesCount: Int,
    @SerializedName("id")
    val id: String,
    @SerializedName("in_reply_to_account_id")
    val inReplyToAccountId: Any,
    @SerializedName("in_reply_to_id")
    val inReplyToId: String,
    @SerializedName("label")
    val label: LabelDto,
    @SerializedName("language")
    val language: Any,
    @SerializedName("liked_by")
    val likedBy: LikedByDto,
    @SerializedName("local")
    val local: Boolean,
    @SerializedName("media_attachments")
    val mediaAttachments: List<Any>,
    @SerializedName("mentions")
    val mentions: List<AccountDto>,
    @SerializedName("muted")
    val muted: Any,
    @SerializedName("parent")
    val parent: List<Any>,
    @SerializedName("pf_type")
    val pfType: String,
    @SerializedName("place")
    val place: Any,
    @SerializedName("poll")
    val poll: Any,
    @SerializedName("reblog")
    val reblog: Any,
    @SerializedName("reblogged")
    val reblogged: Any,
    @SerializedName("reblogs_count")
    val reblogsCount: Int,
    @SerializedName("replies")
    val replies: List<Any>,
    @SerializedName("reply_count")
    val replyCount: Int,
    @SerializedName("sensitive")
    val sensitive: Boolean,
    @SerializedName("shortcode")
    val shortcode: String,
    @SerializedName("spoiler_text")
    val spoilerText: String,
    @SerializedName("taggedPeople")
    val taggedPeople: List<Any>,
    @SerializedName("tags")
    val tags: List<Any>,
    @SerializedName("thread")
    val thread: Boolean,
    @SerializedName("uri")
    val uri: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("_v")
    val v: Int,
    @SerializedName("visibility")
    val visibility: String
)