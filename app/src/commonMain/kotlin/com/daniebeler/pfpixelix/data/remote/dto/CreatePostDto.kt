package com.daniebeler.pfpixelix.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreatePostDto(
    val status: String,
    val media_ids: List<String>,
    val sensitive: Boolean,
    val visibility: String,
    val spoilerText: String?,
    val place_id: String?
)