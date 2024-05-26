package com.daniebeler.pfpixelix.widget.latest_image

import android.content.Context
import android.os.Build
import android.provider.MediaStore
import androidx.compose.runtime.Composable
import androidx.core.net.toUri
import androidx.glance.GlanceId
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.LocalSize
import androidx.glance.appwidget.CircularProgressIndicator
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.currentState
import androidx.glance.state.GlanceStateDefinition
import com.daniebeler.pfpixelix.widget.WidgetColors
import com.daniebeler.pfpixelix.widget.notifications.models.LatestImageStore

class LatestImageWidget : GlanceAppWidget() {

    override var stateDefinition: GlanceStateDefinition<*> = CustomLatestImageStateDefinition

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            Content()
        }
    }

    @Composable
    fun Content() {
        val state = currentState<LatestImageStore>()
        val context = LocalContext.current
        val size = LocalSize.current

        GlanceTheme(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) GlanceTheme.colors
            else WidgetColors.colors
        ) {
            if (state.refreshing || state.latestImageUri.isBlank()) {
                CircularProgressIndicator(color = GlanceTheme.colors.primary)
            } else {
                Image(provider = getImageProvider(state.latestImageUri, context), contentDescription = "latest home timeline picture")
            }
        }
    }
    private fun getImageProvider(path: String, context: Context): ImageProvider {
        val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, path.toUri())
        return ImageProvider(bitmap)
    }
}