package com.daniebeler.pixelix.domain.model

import com.daniebeler.pixelix.data.remote.dto.ReplyDto

data class Reply(
    val id: String,
    val content: String,
    val mentions: List<Account>,
    val account: Account,
    val createdAt: String
)

fun ReplyDto.toReply(): Reply {
    return Reply(
        id = id,
        content = contentText,
        mentions = mentions.map { accountDto -> accountDto.toAccount() },
        account = account.toAccount(),
        createdAt = createdAt
    )
}