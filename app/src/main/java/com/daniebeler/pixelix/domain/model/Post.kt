package com.daniebeler.pixelix.domain.model

import com.daniebeler.pixelix.data.remote.dto.PostDto
import javax.annotation.concurrent.Immutable

@Immutable
data class Post(
    val id: String,
    val mediaAttachments: List<MediaAttachment>,
    val account: Account,
    val tags: List<Tag>,
    val favouritesCount: Int,
    val content: String,
    val replyCount: Int,
    val createdAt: String,
    val url: String,
    val sensitive: Boolean,
    val spoilerText: String,
    val favourited: Boolean
)

fun PostDto.toPost(): Post {
    return Post(
        id = id,
        mediaAttachments = mediaAttachments.map { it.toMediaAttachment() },
        account = account.toAccount(),
        tags = tags.map { it.toTag() },
        favouritesCount = favouritesCount,
        content = contentText ?: "",
        replyCount = replyCount,
        createdAt = createdAt,
        url = url,
        sensitive = sensitive,
        spoilerText = spoilerText ?: "",
        favourited = favourited
    )
}