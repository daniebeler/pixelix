package com.daniebeler.pfpixelix.widget.notifications.models

import kotlinx.serialization.Serializable

@Serializable
data class LatestImageStore(
    val latestImageUri: String = "",
    val postId: String = "",
    val refreshing: Boolean = false,
    val error: String = ""
)