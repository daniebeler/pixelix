package com.daniebeler.pfpixelix.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateMessageDto(
    val to_id: String,
    val message: String,
    val type: String
)
