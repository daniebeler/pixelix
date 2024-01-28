package com.daniebeler.pixelix.data.remote.dto

data class CreateReplyDto(
    val status: String,
    val in_reply_to_id: String
)
