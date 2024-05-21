package com.daniebeler.pfpixelix.widget

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.appwidget.updateAll
import com.daniebeler.pfpixelix.domain.model.Notification
import com.daniebeler.pfpixelix.widget.models.NotificationStoreItem
import com.daniebeler.pfpixelix.widget.models.NotificationsStore

suspend fun updateWidget(notifications: List<Notification>, context: Context) {
    // Iterate through all the available glance id's.
    val notificationsItems: List<NotificationStoreItem> = notifications.map { NotificationStoreItem(it.id, it.account.avatar, it.type) }
    GlanceAppWidgetManager(context).getGlanceIds(NotificationsWidget::class.java).forEach { glanceId ->
        updateAppWidgetState(context = context, definition = CustomNotificationsStateDefinition, glanceId = glanceId) {
            NotificationsStore(
                notifications = notificationsItems
            )
        }
    }
    NotificationsWidget().updateAll(context)
}