package com.daniebeler.pfpixelix.utils

import androidx.compose.runtime.staticCompositionLocalOf
import coil3.PlatformContext
import com.russhwolf.settings.Settings
import okio.Path

expect abstract class KmpUri {
    abstract override fun toString(): String
}

expect abstract class KmpContext
val LocalKmpContext = staticCompositionLocalOf<KmpContext> { error("no KmpContext") }

expect val KmpContext.dataStoreDir: Path
expect val KmpContext.imageCacheDir: Path
expect val KmpContext.pref: Settings
expect val KmpContext.coilContext: PlatformContext

expect fun KmpContext.openUrlInApp(url: String)
expect fun KmpContext.openUrlInBrowser(url: String)
