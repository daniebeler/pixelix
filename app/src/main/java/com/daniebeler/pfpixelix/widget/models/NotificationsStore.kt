package com.daniebeler.pfpixelix.widget.models

import kotlinx.serialization.Serializable

@Serializable
data class NotificationsStore(
    val notifications: List<NotificationStoreItem> = emptyList()
)

@Serializable
data class NotificationStoreItem(
    val id: String,
    val accountImageUrl: String,
    val type: String
)