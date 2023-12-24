package com.daniebeler.pixels.domain.model

import com.daniebeler.pixels.data.remote.dto.ReplyDto

data class Reply(
    val id: String,
    val content: String
)

fun ReplyDto.toReply(): Reply {
    return Reply(
        id = id,
        content = contentText
    )
}