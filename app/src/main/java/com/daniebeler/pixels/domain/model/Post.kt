package com.daniebeler.pixels.domain.model

import com.daniebeler.pixels.data.remote.dto.AccountDto
import com.daniebeler.pixels.data.remote.dto.PostDto

data class Post(
    val id: String,
    val mediaAttachments: List<MediaAttachment>,
    val account: Account,
    val tags: List<Tag>,
    val favouritesCount: Int,
    val content: String,
    val replyCount: Int,
    val createdAt: String
)

fun PostDto.toPost(): Post {
    return Post(
        id = id,
        mediaAttachments = mediaAttachments.map { it.toMediaAttachment() },
        account = account.toAccount(),
        tags = tags.map { it.toTag() },
        favouritesCount = favouritesCount,
        content = content,
        replyCount = replyCount,
        createdAt= createdAt
    )
}