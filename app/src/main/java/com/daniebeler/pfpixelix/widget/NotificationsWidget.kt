package com.daniebeler.pfpixelix.widget

import android.content.Context
import android.os.Build
import android.provider.MediaStore
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.CircularProgressIndicator
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.daniebeler.pfpixelix.MainActivity
import com.daniebeler.pfpixelix.R
import com.daniebeler.pfpixelix.widget.models.NotificationStoreItem
import com.daniebeler.pfpixelix.widget.models.NotificationsStore
import com.daniebeler.pfpixelix.work_manager.NotificationsWorkManager

private val destinationKey = ActionParameters.Key<String>(
    MainActivity.KEY_DESTINATION
)

private val destinationKeyParam = ActionParameters.Key<String>(
    MainActivity.KEY_DESTINATION_PARAM
)

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
        GlanceTheme(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) GlanceTheme.colors
            else WidgetColors.colors
        ) {
            Box(
                GlanceModifier.background(GlanceTheme.colors.background).fillMaxSize()
                    .padding(12.dp), contentAlignment = Alignment.Center
            ) {
                if (notifications.isEmpty()) {
                    CircularProgressIndicator()
                } else {
                    LazyColumn(
                        modifier = GlanceModifier.fillMaxSize()
                    ) {
                        item {
                            Row(modifier = GlanceModifier.fillMaxWidth()){
                                Text(
                                    text = "Notifications",
                                    style = TextStyle(color = GlanceTheme.colors.onBackground)
                                )
                                Spacer(GlanceModifier.defaultWeight())
                                Box(modifier = GlanceModifier.clickable(onClick = actionRunCallback<RefreshAction>()
                                )) {
                                    Image(
                                        provider = ImageProvider(R.drawable.refresh_icon),
                                        contentDescription = "refresh"
                                    )
                                }
                            }
                        }
                        items(notifications) {
                            NotificationItem(notification = it, context = context)
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun NotificationItem(notification: NotificationStoreItem, context: Context) {
        Box(
            modifier = GlanceModifier.clickable(
                actionStartActivity<MainActivity>(
                    actionParametersOf(
                        destinationKey to MainActivity.Companion.StartNavigation.Profile.toString(),
                        destinationKeyParam to notification.accountId
                    )
                )
            )
        ) {
            Column {
                Spacer(GlanceModifier.height(12.dp))
                Row(verticalAlignment = Alignment.Vertical.CenterVertically) {
                    Image(
                        provider = getImageProvider(notification.accountAvatarUri, context),
                        contentDescription = "",
                        modifier = GlanceModifier.height(34.dp).width(34.dp).cornerRadius(34.dp)
                    )
                    Spacer(GlanceModifier.width(6.dp))
                    Text(
                        text = notification.type,
                        style = TextStyle(color = GlanceTheme.colors.onBackground)
                    )
                }
            }
        }

    }

    private fun getImageProvider(path: String, context: Context): ImageProvider {
        val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, path.toUri())
        return ImageProvider(bitmap)
    }
}

class RefreshAction: ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        NotificationsWorkManager(context).executeOnce()
    }
}