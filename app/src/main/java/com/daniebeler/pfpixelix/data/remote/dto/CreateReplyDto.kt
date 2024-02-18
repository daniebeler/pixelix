package com.daniebeler.pfpixelix.data.remote.dto

data class CreateReplyDto(
    val status: String,
    val in_reply_to_id: String
)
