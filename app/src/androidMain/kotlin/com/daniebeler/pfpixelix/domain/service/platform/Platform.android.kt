package com.daniebeler.pfpixelix.domain.service.platform

import android.content.ComponentName
import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.provider.OpenableColumns
import android.util.DisplayMetrics
import android.webkit.MimeTypeMap
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import co.touchlab.kermit.Logger
import coil3.SingletonImageLoader
import coil3.request.ImageRequest
import coil3.toBitmap
import coil3.video.videoFrameMillis
import com.daniebeler.pfpixelix.R
import com.daniebeler.pfpixelix.ui.composables.settings.icon_selection.IconWithName
import com.daniebeler.pfpixelix.utils.KmpContext
import com.daniebeler.pfpixelix.utils.KmpUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject
import java.io.ByteArrayOutputStream

@Inject
actual class Platform actual constructor(
    private val context: KmpContext
) {
    actual fun getPlatformFile(uri: KmpUri): PlatformFile? {
        val f = AndroidFile(uri, context)
        return if (f.getName() != "AndroidFile:unknown") f else null
    }

    actual fun getAppIconManager(): AppIconManager {
        return AndroidAppIconManager(context)
    }
}

private class AndroidFile(
    private val uri: Uri,
    private val context: Context
) : PlatformFile {
    override fun getName(): String = when (uri.scheme) {
        ContentResolver.SCHEME_FILE -> uri.pathSegments.last().substringBeforeLast('.')
        ContentResolver.SCHEME_CONTENT -> context.contentResolver.query(
            uri, null, null, null, null
        )?.use {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            it.moveToFirst()
            it.getString(nameIndex)
        }

        else -> null
    } ?: "AndroidFile:unknown"

    override fun getSize(): Long = when (uri.scheme) {
        ContentResolver.SCHEME_FILE -> context.contentResolver.openFileDescriptor(uri, "r")
            ?.use { it.statSize }

        ContentResolver.SCHEME_CONTENT -> context.contentResolver.query(
            uri, null, null, null, null
        )?.use {
            val nameIndex = it.getColumnIndex(OpenableColumns.SIZE)
            it.moveToFirst()
            it.getLong(nameIndex)
        }

        else -> null
    } ?: 0L

    override fun getMimeType(): String = when (uri.scheme) {
        ContentResolver.SCHEME_FILE -> {
            val fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString())
            MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension.lowercase())
        }

        ContentResolver.SCHEME_CONTENT -> {
            context.contentResolver.getType(uri)
        }

        else -> null
    } ?: "image/*"

    override suspend fun readBytes(): ByteArray = withContext(Dispatchers.IO) {
        context.contentResolver.openInputStream(uri)!!.readBytes()
    }

    override suspend fun getThumbnail(): ByteArray? = withContext(Dispatchers.IO) {
        val bm = try {
            val req = ImageRequest.Builder(context).data(uri).videoFrameMillis(0).build()
            val img = SingletonImageLoader.get(context).execute(req)
            img.image?.toBitmap()
        } catch (e: Exception) {
            Logger.e("AndroidFile.getThumbnail error", e)
            null
        } ?: return@withContext null

        val stream = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.PNG, 100, stream)
        stream.toByteArray()
    }
}

private class AndroidAppIconManager(
    private val context: Context
) : AppIconManager {
    private val appIcons = listOf(
        "com.daniebeler.pfpixelix.MainActivity" to R.mipmap.ic_launcher_02,
        "com.daniebeler.pfpixelix.Icon03" to R.mipmap.ic_launcher_03,
        "com.daniebeler.pfpixelix.Icon01" to R.mipmap.ic_launcher_01,
        "com.daniebeler.pfpixelix.Icon05" to R.mipmap.ic_launcher_05,
        "com.daniebeler.pfpixelix.Icon06" to R.mipmap.ic_launcher_06,
        "com.daniebeler.pfpixelix.Icon07" to R.mipmap.ic_launcher_07,
        "com.daniebeler.pfpixelix.Icon08" to R.mipmap.ic_launcher_08,
        "com.daniebeler.pfpixelix.Icon09" to R.mipmap.ic_launcher_09,
        "com.daniebeler.pfpixelix.Icon04" to R.mipmap.ic_launcher,
    )

    override fun getIcons(): List<IconWithName> {
        fun icon(name: String, id: Int): IconWithName {
            val bm = ResourcesCompat.getDrawableForDensity(
                context.resources, id, DisplayMetrics.DENSITY_XXXHIGH, context.theme
            )!!.let { drawable ->
                drawable.toBitmap(drawable.minimumWidth, drawable.minimumHeight).asImageBitmap()
            }
            val isEnabled = context.packageManager.getComponentEnabledSetting(
                ComponentName(context, name)
            ) == PackageManager.COMPONENT_ENABLED_STATE_ENABLED
            return IconWithName(name, bm, isEnabled)
        }

        return appIcons.map { (name, id) -> icon(name, id) }
    }

    override fun getCurrentIcon(): ImageBitmap? {
        return getIcons().firstOrNull { it.enabled }?.icon
    }

    override fun enableCustomIcon(iconWithName: IconWithName) {
        try {
            context.packageManager.setComponentEnabledSetting(
                ComponentName(context, iconWithName.name),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP
            )
        } catch (e: Error) {
            Logger.e("enableCustomIcon", e)
        }
    }

    override fun disableCustomIcon() {
        appIcons.forEach { (name, id) ->
            context.packageManager.setComponentEnabledSetting(
                ComponentName(context, name),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP
            )
        }
    }
}