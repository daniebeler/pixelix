package com.daniebeler.pfpixelix.utils

import coil3.PlatformContext
import com.russhwolf.settings.Settings
import okio.Path

actual abstract class KmpUri {
    actual abstract override fun toString(): String
}

actual abstract class KmpContext

actual val KmpContext.dataStoreDir: Path
    get() = TODO("Not yet implemented")
actual val KmpContext.imageCacheDir: Path
    get() = TODO("Not yet implemented")
actual val KmpContext.coilContext: PlatformContext
    get() = TODO("Not yet implemented")

actual fun KmpContext.openUrlInApp(url: String) {
}

actual fun KmpContext.openUrlInBrowser(url: String) {
}

actual val KmpContext.pref: Settings
    get() = TODO("Not yet implemented")