package com.daniebeler.pfpixelix.data.remote.dto

data class CreateMessageDto(
    val to_id: String,
    val message: String,
    val type: String
)
