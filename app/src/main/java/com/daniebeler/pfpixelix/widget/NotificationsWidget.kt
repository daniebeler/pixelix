package com.daniebeler.pfpixelix.widget

import android.content.Context
import android.os.Build
import android.provider.MediaStore
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.LocalSize
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.CircularProgressIndicator
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
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
import androidx.glance.text.FontWeight
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

    companion object {
        private val SMALL_SQUARE = DpSize(100.dp, 100.dp)
        private val HORIZONTAL_RECTANGLE = DpSize(250.dp, 100.dp)
        private val BIG_SQUARE = DpSize(250.dp, 250.dp)
    }

    override val sizeMode = SizeMode.Responsive(
        setOf(
            SMALL_SQUARE, HORIZONTAL_RECTANGLE, BIG_SQUARE
        )
    )


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
        val size = LocalSize.current
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
                            Row(
                                modifier = GlanceModifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(modifier = GlanceModifier.clickable(
                                        actionStartActivity<MainActivity>(
                                            actionParametersOf(
                                                destinationKey to MainActivity.Companion.StartNavigation.Notifications.toString(),
                                            )
                                        )
                                    ), verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if (size.height >= BIG_SQUARE.height && size.width >= BIG_SQUARE.width) {
                                        Image(
                                            provider = ImageProvider(R.drawable.pixelix_logo),
                                            contentDescription = null,
                                            modifier = GlanceModifier.width(50.dp).height(50.dp)
                                                .cornerRadius(50.dp)
                                        )
                                        Spacer(GlanceModifier.width(6.dp))
                                        Text(
                                            text = "Notifications", style = TextStyle(
                                                color = GlanceTheme.colors.onBackground,
                                                fontSize = 20.sp
                                            )
                                        )

                                    } else if (size.height <= BIG_SQUARE.height && size.width <= BIG_SQUARE.width) {
                                        Text(
                                            text = "Notifications",
                                            style = TextStyle(color = GlanceTheme.colors.onBackground)
                                        )
                                    }
                                }
                                Spacer(GlanceModifier.defaultWeight())
                                Box(
                                    modifier = GlanceModifier.clickable(
                                        onClick = actionRunCallback<RefreshAction>()
                                    )
                                ) {
                                    Image(
                                        provider = ImageProvider(R.drawable.refresh_icon),
                                        contentDescription = "refresh"
                                    )
                                }
                            }
                            if (size.height >= BIG_SQUARE.height && size.width >= BIG_SQUARE.width) {
                                Spacer(GlanceModifier.height(12.dp))
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
        val size = LocalSize.current
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
                    Column {
                        Row {
                            if (size.width >= BIG_SQUARE.width) {
                                Text(
                                    text = notification.accountUsername, style = TextStyle(
                                        fontWeight = FontWeight.Bold,
                                        color = GlanceTheme.colors.onBackground
                                    )
                                )
                                Spacer(GlanceModifier.width(3.dp))
                            }
                            Text(
                                text = getNotificationText(
                                    notification.type, notification.accountUsername, size.width
                                ), style = TextStyle(color = GlanceTheme.colors.onBackground)
                            )
                        }
                        Text(
                            text = notification.timeAgo,
                            style = TextStyle(color = GlanceTheme.colors.primary, fontSize = 12.sp)
                        )
                    }
                }
            }
        }
    }

    private fun getNotificationText(type: String, accountUsername: String, width: Dp): String {
        return if (width <= SMALL_SQUARE.width) {
            smallNotificationText(type)
        } else {
            bigNotificationText(type, accountUsername)
        }
    }

    private fun smallNotificationText(type: String): String {
        return when (type) {
            "favourite" -> "liked your post"
            "mention" -> "mentioned you"
            "follow" -> "followed you"

            else -> {
                ""
            }
        }
    }

    private fun bigNotificationText(type: String, accountUsername: String): String {
        return when (type) {
            "favourite" -> "liked your post"
            "mention" -> "mentioned you"
            "follow" -> "followed you"

            else -> {
                accountUsername
            }
        }
    }


    private fun getImageProvider(path: String, context: Context): ImageProvider {
        val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, path.toUri())
        return ImageProvider(bitmap)
    }
}

class RefreshAction : ActionCallback {
    override suspend fun onAction(
        context: Context, glanceId: GlanceId, parameters: ActionParameters
    ) {
        NotificationsWorkManager(context).executeOnce()
    }
}