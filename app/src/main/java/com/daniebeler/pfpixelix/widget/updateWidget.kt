package com.daniebeler.pfpixelix.widget

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.appwidget.updateAll
import com.daniebeler.pfpixelix.widget.models.NotificationStoreItem
import com.daniebeler.pfpixelix.widget.models.NotificationsStore

suspend fun updateWidget(notifications: List<NotificationStoreItem>, context: Context) {
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