package com.daniebeler.pixelix.domain.model

import com.daniebeler.pixelix.data.remote.dto.ReplyDto

data class Reply(
    val id: String,
    val content: String,
    val mentions: List<Account>
)

fun ReplyDto.toReply(): Reply {
    return Reply(
        id = id,
        content = contentText,
        mentions = mentions.map { accountDto -> accountDto.toAccount() }
    )
}