package com.daniebeler.pfpixelix.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NewPost(
    @SerialName("status") val status: String,
    @SerialName("media_ids") val mediaIds: List<String>,
    @SerialName("sensitive") val sensitive: Boolean,
    @SerialName("visibility") val visibility: String,
    @SerialName("spoilerText") val spoilerText: String?,
    @SerialName("place_id") val placeId: String?
)