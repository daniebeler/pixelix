package com.daniebeler.pfpixelix.widget.notifications

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.appwidget.updateAll
import com.daniebeler.pfpixelix.widget.notifications.models.NotificationStoreItem
import com.daniebeler.pfpixelix.widget.notifications.models.NotificationsStore

suspend fun updateNotificationsWidget(notifications: List<NotificationStoreItem>, context: Context) {
    GlanceAppWidgetManager(context).getGlanceIds(NotificationsWidget::class.java)
        .forEach { glanceId ->
            updateAppWidgetState(
                context = context,
                definition = CustomNotificationsStateDefinition,
                glanceId = glanceId
            ) {
                NotificationsStore(
                    notifications = notifications
                )
            }
        }
    NotificationsWidget().updateAll(context)
}