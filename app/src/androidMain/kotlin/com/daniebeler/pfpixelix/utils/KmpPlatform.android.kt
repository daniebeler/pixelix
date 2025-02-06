package com.daniebeler.pfpixelix.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import coil3.PlatformContext
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import okio.Path
import okio.Path.Companion.toPath
import java.io.File

actual typealias KmpUri = Uri
actual typealias KmpContext = Context

actual val KmpContext.dataStoreDir: Path
    get() = File(applicationContext.filesDir, "datastore").path.toPath()
actual val KmpContext.imageCacheDir: Path get() = cacheDir.path.toPath()

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