package com.daniebeler.pfpixelix.utils

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri
import co.touchlab.kermit.Logger
import coil3.PlatformContext
import com.daniebeler.pfpixelix.domain.model.AppThemeMode.AMOLED
import com.daniebeler.pfpixelix.domain.model.AppThemeMode.DARK
import com.daniebeler.pfpixelix.domain.model.AppThemeMode.LIGHT
import com.daniebeler.pfpixelix.widget.notifications.NotificationWidgetReceiver
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import io.github.vinceglb.filekit.core.PlatformFile
import okio.Path
import okio.Path.Companion.toPath
import java.io.File

actual typealias KmpUri = Uri
actual fun String.toKmpUri(): KmpUri = this.toUri()
actual val EmptyKmpUri = Uri.EMPTY
actual fun PlatformFile.toKmpUri(): KmpUri = this.uri

actual typealias KmpContext = Context

actual val KmpContext.dataStoreDir: Path
    get() = File(applicationContext.filesDir, "datastore").path.toPath()
actual val KmpContext.imageCacheDir: Path get() = cacheDir.path.toPath().resolve("image_cache")

actual val KmpContext.coilContext: PlatformContext get() = this

actual fun KmpContext.openUrlInApp(url: String) {
    val intent = CustomTabsIntent.Builder().build()
    intent.launchUrl(this, Uri.parse(url))
}

actual fun KmpContext.openUrlInBrowser(url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    startActivity(intent)
}

actual val KmpContext.appVersionName: String
    get() = try {
        packageManager.getPackageInfo(packageName, 0).versionName
    } catch (e: Exception) {
        Logger.e("appVersionName", e)
        null
    }.orEmpty()

actual fun KmpContext.setDefaultNightMode(mode: Int) {
    AppCompatDelegate.setDefaultNightMode(
        when (mode) {
            LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
            AMOLED, DARK -> AppCompatDelegate.MODE_NIGHT_YES
            else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }
    )
}

actual fun KmpContext.getCacheSizeInBytes(): Long {
    return cacheDir.walkBottomUp().fold(0L) { acc, file -> acc + file.length() }
}

actual fun KmpContext.cleanCache() {
    cacheDir.deleteRecursively()
}

actual fun KmpContext.pinWidget() {
    val appWidgetManager = AppWidgetManager.getInstance(this)
    val myProvider = ComponentName(this, NotificationWidgetReceiver::class.java)

    if (appWidgetManager.isRequestPinAppWidgetSupported) {
        appWidgetManager.requestPinAppWidget(myProvider, null, null)
    }
}

actual fun isAbleToDownloadImage(): Boolean =
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

actual fun KmpUri.getPlatformUriObject(): Any = this