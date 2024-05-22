package com.daniebeler.pfpixelix.widget

import android.content.Context
import android.provider.MediaStore
import androidx.compose.runtime.Composable
import androidx.core.net.toUri
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.appwidget.CircularProgressIndicator
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.text.Text
import com.daniebeler.pfpixelix.R
import com.daniebeler.pfpixelix.widget.models.NotificationStoreItem
import com.daniebeler.pfpixelix.widget.models.NotificationsStore

class NotificationsWidget : GlanceAppWidget() {
    override var stateDefinition: GlanceStateDefinition<*> = CustomNotificationsStateDefinition
    override suspend fun provideGlance(context: Context, id: GlanceId) {

        // In this method, load data needed to render the AppWidget.
        // Use `withContext` to switch to another thread for long running
        // operations.

        provideContent {
            // create your AppWidget here
            Content()
        }
    }

    @Composable
    fun Content() {
        val state = currentState<NotificationsStore>()
        val notifications = state.notifications
        val context = LocalContext.current
        GlanceTheme {
            if (notifications.isEmpty()) {
                CircularProgressIndicator()
            } else {
                Column(
                    modifier = GlanceModifier.fillMaxSize().background(R.color.teal_700),
                ) {
                    notifications.forEach{
                        NotificationItem(notification = it, context = context)
                    }

                }
            }
        }
    }

    @Composable
    private fun NotificationItem(notification: NotificationStoreItem, context: Context) {
        Row {
            if (notification.accountAvatarUri.isNotBlank()) {
                Image(
                    provider = getImageProvider(notification.accountAvatarUri, context),
                    contentDescription = ""
                )
            }
            Text(text = notification.type)
        }

    }

    private fun getImageProvider(path: String, context: Context): ImageProvider {
        val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, path.toUri())
        return ImageProvider(bitmap)
    }
}