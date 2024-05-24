package com.daniebeler.pfpixelix.widget.models

import kotlinx.serialization.Serializable

@Serializable
data class NotificationsStore(
    val notifications: List<NotificationStoreItem> = emptyList()
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