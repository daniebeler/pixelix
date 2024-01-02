package com.daniebeler.pixelix.domain.model

import com.daniebeler.pixelix.data.remote.dto.ApiReplyElementDto

data class ApiReplyElement(
    val data: List<Reply>
)

fun ApiReplyElementDto.toApiReplyElement(): ApiReplyElement {
    return ApiReplyElement(
        data = data.map { it.toReply() }
    )
}