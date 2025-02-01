package com.daniebeler.pfpixelix.data.remote.dto


import com.daniebeler.pfpixelix.domain.model.Post
import com.daniebeler.pfpixelix.utils.HtmlToText.htmlToText
import com.google.gson.annotations.SerializedName

data class PostDto(
    @SerializedName("account") val account: AccountDto,
    @SerializedName("application") val application: ApplicationDto,
    @SerializedName("comments_disabled") val commentsDisabled: Boolean,
    @SerializedName("content") val content: String?,
    @SerializedName("content_text") val contentText: String?,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("edited_at") val editedAt: Any,
    @SerializedName("emojis") val emojis: List<Any>,
    @SerializedName("favourited") val favourited: Boolean,
    @SerializedName("favourites_count") val favouritesCount: Int,
    @SerializedName("id") val id: String,
    @SerializedName("in_reply_to_account_id") val inReplyToAccountId: Any,
    @SerializedName("in_reply_to_id") val inReplyToId: String?,
    @SerializedName("label") val label: LabelDto,
    @SerializedName("language") val language: Any,
    @SerializedName("liked_by") val likedBy: LikedByDto?,
    @SerializedName("local") val local: Boolean,
    @SerializedName("media_attachments") val mediaAttachments: List<MediaAttachmentDto>,
    @SerializedName("mentions") val mentions: List<AccountDto>,
    @SerializedName("muted") val muted: Any,
    @SerializedName("parent") val parent: List<Any>,
    @SerializedName("pf_type") val pfType: String,
    @SerializedName("place") val place: PlaceDto?,
    @SerializedName("poll") val poll: Any,
    @SerializedName("reblog") val reblog: PostDto?,
    @SerializedName("reblogged") val reblogged: Boolean,
    @SerializedName("reblogs_count") val reblogsCount: Int,
    @SerializedName("replies") val replies: List<Any>,
    @SerializedName("reply_count") val replyCount: Int,
    @SerializedName("sensitive") val sensitive: Boolean,
    @SerializedName("shortcode") val shortcode: String,
    @SerializedName("spoiler_text") val spoilerText: String?,
    @SerializedName("taggedPeople") val taggedPeople: List<Any>,
    @SerializedName("tags") val tags: List<TagDto>,
    @SerializedName("thread") val thread: Boolean,
    @SerializedName("uri") val uri: String,
    @SerializedName("url") val url: String,
    @SerializedName("_v") val v: Int,
    @SerializedName("visibility") val visibility: String,
    @SerializedName("bookmarked") val bookmarked: Boolean,
) : DtoInterface<Post> {
    override fun toModel(): Post {
        if (reblog != null) {
            return Post(
                rebloggedBy = account.toModel(),
                id = id,
                reblogId = reblog.id,
                mediaAttachments = reblog.mediaAttachments.map { it.toModel() },
                account = reblog.account.toModel(),
                tags = reblog.tags.map { it.toModel() },
                favouritesCount = reblog.favouritesCount,
                content = reblog.content?.let { htmlToText(it) } ?: reblog.contentText ?: "",
                replyCount = reblog.replyCount,
                createdAt = reblog.createdAt,
                url = reblog.url,
                sensitive = reblog.sensitive,
                spoilerText = reblog.spoilerText ?: "",
                favourited = reblog.favourited,
                reblogged = reblog.reblogged,
                bookmarked = reblog.bookmarked,
                mentions = reblog.mentions.map { it.toModel() },
                place = reblog.place?.toModel(),
                likedBy = reblog.likedBy?.toModel(),
                visibility = reblog.visibility,
                inReplyToId = reblog.inReplyToId
            )
        } else {
            return Post(
                id = id,
                mediaAttachments = mediaAttachments.map { it.toModel() },
                account = account.toModel(),
                tags = tags.map { it.toModel() },
                favouritesCount = favouritesCount,
                content = content?.let { htmlToText(it) } ?: contentText ?: "",
                replyCount = replyCount,
                createdAt = createdAt,
                url = url,
                sensitive = sensitive,
                spoilerText = spoilerText ?: "",
                favourited = favourited,
                reblogged = reblogged,
                bookmarked = bookmarked,
                mentions = mentions.map { it.toModel() },
                place = place?.toModel(),
                likedBy = likedBy?.toModel(),
                visibility = visibility,
                inReplyToId = inReplyToId
            )
        }
    }
}

