package com.daniebeler.pfpixelix.utils

import androidx.compose.runtime.staticCompositionLocalOf
import coil3.PlatformContext
import com.daniebeler.pfpixelix.ui.composables.settings.icon_selection.IconWithName
import com.russhwolf.settings.Settings
import io.github.vinceglb.filekit.core.PlatformFile
import okio.Path

expect abstract class KmpUri {
    abstract override fun toString(): String
}
expect fun String.toKmpUri(): KmpUri
expect fun PlatformFile.toKmpUri(): KmpUri
expect val EmptyKmpUri: KmpUri
expect fun KmpUri.getPlatformUriObject(): Any

expect abstract class KmpContext
val LocalKmpContext = staticCompositionLocalOf<KmpContext> { error("no KmpContext") }

expect val KmpContext.dataStoreDir: Path
expect val KmpContext.imageCacheDir: Path
expect val KmpContext.pref: Settings
expect val KmpContext.coilContext: PlatformContext
expect val KmpContext.appVersionName: String

expect fun KmpContext.openUrlInApp(url: String)
expect fun KmpContext.openUrlInBrowser(url: String)
expect fun KmpContext.setDefaultNightMode(mode: Int)
expect fun KmpContext.getCacheSizeInBytes(): Long
expect fun KmpContext.cleanCache()
expect fun KmpContext.getAppIcons(): List<IconWithName>
expect fun KmpContext.enableCustomIcon(iconWithName: IconWithName)
expect fun KmpContext.disableCustomIcon()
expect fun KmpContext.pinWidget()

expect fun isAbleToDownloadImage(): Boolean
