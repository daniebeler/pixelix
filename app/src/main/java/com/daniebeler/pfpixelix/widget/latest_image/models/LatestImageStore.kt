package com.daniebeler.pfpixelix.widget.notifications.models

import kotlinx.serialization.Serializable

@Serializable
data class LatestImageStore(
    val latestImageUri: String = "",
    val refreshing: Boolean = false
)