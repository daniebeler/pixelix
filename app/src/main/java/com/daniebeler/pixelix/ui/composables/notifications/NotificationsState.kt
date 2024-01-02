package com.daniebeler.pixelix.ui.composables.notifications

import com.daniebeler.pixelix.domain.model.Notification

data class NotificationsState(
    val isLoading: Boolean = false,
    val notifications: List<Notification> = emptyList(),
    val error: String = ""
)