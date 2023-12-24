package com.daniebeler.pixels.domain.model

import com.daniebeler.pixels.data.remote.dto.ApiReplyElementDto

data class ApiReplyElement(
    val data: List<Reply>
)

fun ApiReplyElementDto.toApiReplyElement(): ApiReplyElement {
    return ApiReplyElement(
        data = data.map { it.toReply() }
    )
}