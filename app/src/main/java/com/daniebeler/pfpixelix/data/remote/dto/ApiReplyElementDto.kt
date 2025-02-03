package com.daniebeler.pfpixelix.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiReplyElementDto(
    @SerialName("data") val data: List<ReplyDto>
)
