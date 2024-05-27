package com.daniebeler.pfpixelix.widget.latest_image

import android.content.Context
import android.os.Build
import android.provider.MediaStore
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.core.graphics.drawable.toBitmap
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
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Column
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.daniebeler.pfpixelix.MainActivity
import com.daniebeler.pfpixelix.widget.WidgetColors
import com.daniebeler.pfpixelix.widget.notifications.models.LatestImageStore


class LatestImageWidget : GlanceAppWidget() {

    override var stateDefinition: GlanceStateDefinition<*> = CustomLatestImageStateDefinition

    private val destinationKey = ActionParameters.Key<String>(
        MainActivity.KEY_DESTINATION
    )
    private val destinationKeyParam = ActionParameters.Key<String>(
        MainActivity.KEY_DESTINATION_PARAM
    )

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            Content()
        }
    }

    @Composable
    fun Content() {
        val state = currentState<LatestImageStore>()
        val context = LocalContext.current

        GlanceTheme(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) GlanceTheme.colors
            else WidgetColors.colors
        ) {
            if (state.latestImageUri.isNotBlank()) {
                Image(
                    provider = getImageProvider(state.latestImageUri, context),
                    contentDescription = "latest home timeline picture",
                    modifier = GlanceModifier.fillMaxSize().clickable(
                        actionStartActivity<MainActivity>(
                            actionParametersOf(
                                destinationKey to MainActivity.Companion.StartNavigation.Post.toString(),
                                destinationKeyParam to state.postId
                            )
                        )
                    )
                )
            } else {
                Column(GlanceModifier.fillMaxSize().background(GlanceTheme.colors.background).padding(12.dp)) {
                    Text(text = "Latest Image Widget", style = TextStyle(color = GlanceTheme.colors.onBackground))
                    Spacer(GlanceModifier.height(12.dp))
                    if (state.error.isNotBlank()) {
                        Text(text = state.error, style = TextStyle(color = GlanceTheme.colors.error))
                    } else if (state.refreshing || state.latestImageUri.isBlank()) {
                        CircularProgressIndicator(color = GlanceTheme.colors.primary)
                    }
                }
            }
        }
    }

    private fun getImageProvider(path: String, context: Context): ImageProvider {
        val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, path.toUri())
        val roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(context.resources, bitmap)
        roundedBitmapDrawable.cornerRadius = 50.dp.value
        return ImageProvider(roundedBitmapDrawable.current.toBitmap())
    }
}