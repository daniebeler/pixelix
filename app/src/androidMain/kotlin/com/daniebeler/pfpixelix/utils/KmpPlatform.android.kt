package com.daniebeler.pfpixelix.utils

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.util.DisplayMetrics
import androidx.appcompat.app.AppCompatDelegate
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.net.toUri
import co.touchlab.kermit.Logger
import coil3.PlatformContext
import com.daniebeler.pfpixelix.LoginActivity
import com.daniebeler.pfpixelix.R
import com.daniebeler.pfpixelix.ui.composables.settings.icon_selection.IconWithName
import com.daniebeler.pfpixelix.utils.ThemePrefUtil.AMOLED
import com.daniebeler.pfpixelix.utils.ThemePrefUtil.DARK
import com.daniebeler.pfpixelix.utils.ThemePrefUtil.LIGHT
import com.daniebeler.pfpixelix.widget.notifications.NotificationWidgetReceiver
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import okio.Path
import okio.Path.Companion.toPath
import java.io.File

actual typealias KmpUri = Uri
actual fun String.toKmpUri(): KmpUri = this.toUri()
actual val EmptyKmpUri = Uri.EMPTY

actual typealias KmpContext = Context

actual val KmpContext.dataStoreDir: Path
    get() = File(applicationContext.filesDir, "datastore").path.toPath()
actual val KmpContext.imageCacheDir: Path get() = cacheDir.path.toPath().resolve("image_cache")

actual val KmpContext.pref: Settings
    get() = SharedPreferencesSettings(
        applicationContext.getSharedPreferences(
            applicationContext.packageName + "_preferences",
            Context.MODE_PRIVATE
        )
    )

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

actual fun KmpContext.openLoginScreen(isAbleToGotBack: Boolean) {
    val intent = Intent(this, LoginActivity::class.java)
    if (!isAbleToGotBack) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
    }
    startActivity(intent)
}

actual fun KmpContext.getCacheSizeInBytes(): Long {
    return cacheDir.walkBottomUp().fold(0L) { acc, file -> acc + file.length() }
}

actual fun KmpContext.cleanCache() {
    cacheDir.deleteRecursively()
}

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

actual fun KmpContext.getAppIcons(): List<IconWithName> {
    fun icon(name: String, id: Int): IconWithName {
        val bm = ResourcesCompat.getDrawableForDensity(
            resources, id, DisplayMetrics.DENSITY_XXXHIGH, theme
        )!!.let { drawable ->
            drawable.toBitmap(drawable.minimumWidth, drawable.minimumHeight).asImageBitmap()
        }
        val isEnabled = packageManager.getComponentEnabledSetting(
            ComponentName(this, name)
        ) == PackageManager.COMPONENT_ENABLED_STATE_ENABLED
        return IconWithName(name, bm, isEnabled)
    }

    return appIcons.map { (name, id) -> icon(name, id) }
}

actual fun KmpContext.enableCustomIcon(iconWithName: IconWithName) {
    try {
        packageManager.setComponentEnabledSetting(
            ComponentName(this, iconWithName.name),
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )
    } catch (e: Error) {
        Logger.e("enableCustomIcon", e)
    }
}

actual fun KmpContext.disableCustomIcon() {
    appIcons.forEach { (name, id) ->
        packageManager.setComponentEnabledSetting(
            ComponentName(this, name),
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            PackageManager.DONT_KILL_APP
        )
    }
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