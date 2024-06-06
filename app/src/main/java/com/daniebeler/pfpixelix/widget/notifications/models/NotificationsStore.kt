package com.daniebeler.pfpixelix.widget.notifications.models

import kotlinx.serialization.Serializable

@Serializable
data class NotificationsStore(
    val notifications: List<NotificationStoreItem> = emptyList(),
    val refreshing: Boolean = false,
    val error: String = ""
)

@Serializable
data class NotificationStoreItem(
    val id: String,
    val accountAvatarUrl: String,
    val accountAvatarUri: String,
    val accountId: String,
    val accountUsername: String,
    val timeAgo: String,
    val type: String
)