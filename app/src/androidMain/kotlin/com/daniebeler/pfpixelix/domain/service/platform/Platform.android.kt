package com.daniebeler.pfpixelix.domain.service.platform

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.DisplayMetrics
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import co.touchlab.kermit.Logger
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.request.ImageRequest
import coil3.request.SuccessResult
import coil3.request.allowHardware
import coil3.toBitmap
import coil3.video.videoFrameMillis
import com.daniebeler.pfpixelix.MyApplication
import com.daniebeler.pfpixelix.R
import com.daniebeler.pfpixelix.domain.service.preferences.UserPreferences
import com.daniebeler.pfpixelix.ui.composables.settings.icon_selection.IconWithName
import com.daniebeler.pfpixelix.utils.KmpContext
import com.daniebeler.pfpixelix.utils.KmpUri
import com.daniebeler.pfpixelix.widget.notifications.NotificationWidgetReceiver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import me.tatarka.inject.annotations.Inject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException

@Inject
actual class Platform actual constructor(
    private val context: KmpContext,
    private val prefs: UserPreferences
) {
    actual fun getPlatformFile(uri: KmpUri): PlatformFile? {
        val f = AndroidFile(uri, context)
        return if (f.getName() != "AndroidFile:unknown") f else null
    }

    actual fun getAppIconManager(): AppIconManager {
        return AndroidAppIconManager(context)
    }

    actual fun openUrl(url: String) {
        val activity = MyApplication.currentActivity?.get()
        if (activity != null) {
            if (prefs.useInAppBrowser) {
                val intent = CustomTabsIntent.Builder().build()
                intent.launchUrl(activity, Uri.parse(url))
            } else {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                activity.startActivity(intent)
            }
        } else {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    actual fun shareText(text: String) {
        val activity = MyApplication.currentActivity?.get()
        if (activity != null) {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, text)
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            activity.startActivity(shareIntent)
        }
    }

    actual fun getAppVersion(): String {
        return try {
            context.packageManager.getPackageInfo(context.packageName, 0).versionName
        } catch (e: Exception) {
            Logger.e("appVersionName", e)
            null
        }.orEmpty()
    }

    actual fun pinWidget() {
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val myProvider = ComponentName(context, NotificationWidgetReceiver::class.java)

        if (appWidgetManager.isRequestPinAppWidgetSupported) {
            appWidgetManager.requestPinAppWidget(myProvider, null, null)
        }
    }

    actual fun downloadImageToGallery(name: String?, url: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            var uri: Uri? = null
            val saveImageRoutine = CoroutineScope(Dispatchers.Default).launch {

                val bitmap: Bitmap? = urlToBitmap(url, context)
                if (bitmap == null) {
                    cancel("an error occured when downloading the image")
                    return@launch
                }

                println(bitmap.toString())

                uri = saveImageToMediaStore(context, generateUniqueName(name, false, context), bitmap!!)
                if (uri == null) {
                    cancel("an error occured when saving the image")
                    return@launch
                }
            }

            saveImageRoutine.invokeOnCompletion { throwable ->
                CoroutineScope(Dispatchers.Main).launch {
                    uri?.let {
                        Toast.makeText(context, "Stored at: " + uri.toString(), Toast.LENGTH_LONG)
                            .show()
                    } ?: throwable?.let {
                        Toast.makeText(
                            context, "an error occurred downloading the image", Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    private fun generateUniqueName(
        imageName: String?, returnFullPath: Boolean, context: KmpContext
    ): String {

        val filename = "${imageName}_${Clock.System.now().epochSeconds}"

        if (returnFullPath) {
            val directory: File = context.getDir("zest", Context.MODE_PRIVATE)
            return "$directory/$filename"
        } else {
            return filename
        }
    }

    private suspend fun urlToBitmap(
        imageURL: String,
        context: KmpContext,
    ): Bitmap? {
        val loader = ImageLoader(context)
        val request = ImageRequest.Builder(context).data(imageURL).allowHardware(false).build()
        val result = loader.execute(request)
        if (result is SuccessResult) {
            return result.image.toBitmap()
        }
        return null
    }

    private fun saveImageToMediaStore(context: KmpContext, displayName: String, bitmap: Bitmap): Uri? {
        val imageCollections = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

        val imageDetails = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, displayName)
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.Images.Media.IS_PENDING, 1)
            }
        }

        val resolver = context.applicationContext.contentResolver
        val imageContentUri = resolver.insert(imageCollections, imageDetails) ?: return null

        return try {
            resolver.openOutputStream(imageContentUri, "w").use { os ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, os!!)
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                imageDetails.clear()
                imageDetails.put(MediaStore.Images.Media.IS_PENDING, 0)
                resolver.update(imageContentUri, imageDetails, null, null)
            }

            imageContentUri
        } catch (e: FileNotFoundException) {
            // Some legacy devices won't create directory for the Uri if dir not exist, resulting in
            // a FileNotFoundException. To resolve this issue, we should use the File API to save the
            // image, which allows us to create the directory ourselves.
            null
        }
    }

    actual fun getCacheSizeInBytes(): Long {
        return context.cacheDir.walkBottomUp().fold(0L) { acc, file -> acc + file.length() }
    }
    actual fun cleanCache() {
        context.cacheDir.deleteRecursively()
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