package com.daniebeler.pixels.api.models

import com.google.gson.annotations.SerializedName

data class ReplyDTO (
    val id: String,
    @SerializedName("content_text")
    val content: String
)

data class ApiReplyElement (
    @SerializedName("data")
    val data: List<ReplyDTO>
)


fun ReplyDTO.toModel() = Reply(
    id = this.id,
    content = this.content
)