package com.daniebeler.pixelix.domain.model

import com.daniebeler.pixelix.data.remote.dto.NotificationDto
import com.daniebeler.pixelix.utils.TimeAgo

data class Notification(
    val account: Account,
    val id: String,
    val type: String,
    val post: Post?,
    var createdAt: String,
    var timeAgo: String
)

fun NotificationDto.toNotification(): Notification {
    return Notification(
        account = account.toAccount(),
        id = id,
        type = type,
        post = post?.toPost(),
        createdAt = createdAt,
        timeAgo = TimeAgo().covertTimeToText(createdAt)
    )
}