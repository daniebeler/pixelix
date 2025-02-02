package com.daniebeler.pfpixelix.data.remote.dto


import com.daniebeler.pfpixelix.domain.model.Reply
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class ReplyDto(
    @SerialName("account") val account: AccountDto,
    @SerialName("application") val application: ApplicationDto?,
    @SerialName("comments_disabled") val commentsDisabled: Boolean?,
    @SerialName("content") val content: String?,
    @SerialName("content_text") val contentText: String,
    @SerialName("created_at") val createdAt: String,
    @SerialName("edited_at") val editedAt: String?,
    @SerialName("emojis") val emojis: List<JsonElement?>?,
    @SerialName("favourited") val favourited: Boolean?,
    @SerialName("favourites_count") val favouritesCount: Int?,
    @SerialName("id") val id: String,
    @SerialName("in_reply_to_account_id") val inReplyToAccountId: JsonElement?,
    @SerialName("in_reply_to_id") val inReplyToId: String?,
    @SerialName("label") val label: LabelDto?,
    @SerialName("language") val language: JsonElement?,
    @SerialName("liked_by") val likedBy: LikedByDto,
    @SerialName("local") val local: Boolean?,
    @SerialName("media_attachments") val mediaAttachments: List<JsonElement?>?,
    @SerialName("mentions") val mentions: List<AccountDto>,
    @SerialName("muted") val muted: JsonElement?,
    @SerialName("parent") val parent: List<JsonElement?>?,
    @SerialName("pf_type") val pfType: String?,
    @SerialName("place") val place: JsonElement?,
    @SerialName("poll") val poll: JsonElement?,
    @SerialName("reblog") val reblog: JsonElement?,
    @SerialName("reblogged") val reblogged: JsonElement?,
    @SerialName("reblogs_count") val reblogsCount: Int?,
    @SerialName("replies") val replies: List<JsonElement?>?,
    @SerialName("reply_count") val replyCount: Int,
    @SerialName("sensitive") val sensitive: Boolean?,
    @SerialName("shortcode") val shortcode: String?,
    @SerialName("spoiler_text") val spoilerText: String?,
    @SerialName("taggedPeople") val taggedPeople: List<JsonElement?>?,
    @SerialName("tags") val tags: List<JsonElement?>?,
    @SerialName("thread") val thread: Boolean?,
    @SerialName("uri") val uri: String?,
    @SerialName("url") val url: String?,
    @SerialName("_v") val v: Int?,
    @SerialName("visibility") val visibility: String?
) : DtoInterface<Reply> {
    override fun toModel(): Reply {
        return Reply(
            id = id,
            content = contentText,
            mentions = mentions.map { accountDto -> accountDto.toModel() },
            account = account.toModel(),
            createdAt = createdAt,
            replyCount = replyCount,
            likedBy = likedBy.toModel()
        )
    }
}