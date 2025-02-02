package com.daniebeler.pfpixelix.data.remote.dto


import com.daniebeler.pfpixelix.domain.model.Post
import com.daniebeler.pfpixelix.utils.HtmlToText.htmlToText
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class PostDto(
    @SerialName("account") val account: AccountDto,
    @SerialName("application") val application: ApplicationDto?,
    @SerialName("comments_disabled") val commentsDisabled: Boolean?,
    @SerialName("content") val content: String?,
    @SerialName("content_text") val contentText: String?,
    @SerialName("created_at") val createdAt: String?,
    @SerialName("edited_at") val editedAt: JsonElement?,
    @SerialName("emojis") val emojis: List<JsonElement?>?,
    @SerialName("favourited") val favourited: Boolean?,
    @SerialName("favourites_count") val favouritesCount: Int?,
    @SerialName("id") val id: String?,
    @SerialName("in_reply_to_account_id") val inReplyToAccountId: JsonElement?,
    @SerialName("in_reply_to_id") val inReplyToId: String?,
    @SerialName("label") val label: LabelDto?,
    @SerialName("language") val language: JsonElement?,
    @SerialName("liked_by") val likedBy: LikedByDto?,
    @SerialName("local") val local: Boolean?,
    @SerialName("media_attachments") val mediaAttachments: List<MediaAttachmentDto>?,
    @SerialName("mentions") val mentions: List<AccountDto>?,
    @SerialName("muted") val muted: JsonElement?,
    @SerialName("parent") val parent: List<JsonElement?>?,
    @SerialName("pf_type") val pfType: String?,
    @SerialName("place") val place: PlaceDto?,
    @SerialName("poll") val poll: JsonElement?,
    @SerialName("reblog") val reblog: PostDto?,
    @SerialName("reblogged") val reblogged: Boolean?,
    @SerialName("reblogs_count") val reblogsCount: Int?,
    @SerialName("replies") val replies: List<JsonElement?>?,
    @SerialName("reply_count") val replyCount: Int?,
    @SerialName("sensitive") val sensitive: Boolean?,
    @SerialName("shortcode") val shortcode: String?,
    @SerialName("spoiler_text") val spoilerText: String?,
    @SerialName("taggedPeople") val taggedPeople: List<JsonElement?>?,
    @SerialName("tags") val tags: List<TagDto>?,
    @SerialName("thread") val thread: Boolean?,
    @SerialName("uri") val uri: String?,
    @SerialName("url") val url: String?,
    @SerialName("_v") val v: Int?,
    @SerialName("visibility") val visibility: String?,
    @SerialName("bookmarked") val bookmarked: Boolean?,
) : DtoInterface<Post> {
    override fun toModel(): Post {
        if (reblog != null) {
            return Post(
                rebloggedBy = account.toModel(),
                id = id ?: "",
                reblogId = reblog.id,
                mediaAttachments = reblog.mediaAttachments?.map { it.toModel() }.orEmpty(),
                account = reblog.account.toModel(),
                tags = reblog.tags?.map { it.toModel() }.orEmpty(),
                favouritesCount = reblog.favouritesCount ?: 0,
                content = reblog.contentText?.let { it.takeIf { it.isNotEmpty() } }
                    ?: reblog.content?.let { htmlToText(it) } ?: "",
                replyCount = reblog.replyCount ?: 0,
                createdAt = reblog.createdAt ?: "",
                url = reblog.url ?: "",
                sensitive = reblog.sensitive ?: false,
                spoilerText = reblog.spoilerText ?: "",
                favourited = reblog.favourited ?: false,
                reblogged = reblog.reblogged ?: false,
                bookmarked = reblog.bookmarked ?: false,
                mentions = reblog.mentions?.map { it.toModel() }.orEmpty(),
                place = reblog.place?.toModel(),
                likedBy = reblog.likedBy?.toModel(),
                visibility = reblog.visibility ?: "",
                inReplyToId = reblog.inReplyToId
            )
        } else {
            return Post(
                id = id ?: "",
                mediaAttachments = mediaAttachments?.map { it.toModel() }.orEmpty(),
                account = account.toModel(),
                tags = tags?.map { it.toModel() }.orEmpty(),
                favouritesCount = favouritesCount ?: 0,
                content = contentText?.let { it.takeIf { it.isNotEmpty() } }
                    ?: content?.let { htmlToText(it) } ?: "",
                replyCount = replyCount ?: 0,
                createdAt = createdAt ?: "",
                url = url ?: "",
                sensitive = sensitive ?: false,
                spoilerText = spoilerText ?: "",
                favourited = favourited ?: false,
                reblogged = reblogged ?: false,
                bookmarked = bookmarked ?: false,
                mentions = mentions?.map { it.toModel() }.orEmpty(),
                place = place?.toModel(),
                likedBy = likedBy?.toModel(),
                visibility = visibility ?: "",
                inReplyToId = inReplyToId
            )
        }
    }
}

