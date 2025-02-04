package com.daniebeler.pfpixelix.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateReplyDto(
    val status: String,
    val in_reply_to_id: String
)
