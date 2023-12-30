package com.daniebeler.pixels.ui.composables.notifications

import com.daniebeler.pixels.domain.model.Notification

data class NotificationsState(
    val isLoading: Boolean = false,
    val notifications: List<Notification> = emptyList(),
    val error: String = ""
)