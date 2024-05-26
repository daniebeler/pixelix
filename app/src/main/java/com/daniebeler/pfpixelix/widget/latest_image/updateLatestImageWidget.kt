package com.daniebeler.pfpixelix.widget.latest_image

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.appwidget.updateAll
import com.daniebeler.pfpixelix.widget.notifications.models.LatestImageStore

suspend fun updateLatestImageWidget(uri: String, context: Context) {
    GlanceAppWidgetManager(context).getGlanceIds(LatestImageWidget::class.java)
        .forEach { glanceId ->
            updateAppWidgetState(
                context = context,
                definition = CustomLatestImageStateDefinition,
                glanceId = glanceId
            ) {
                LatestImageStore(
                    latestImageUri = uri
                )
            }
        }
    LatestImageWidget().updateAll(context)
}