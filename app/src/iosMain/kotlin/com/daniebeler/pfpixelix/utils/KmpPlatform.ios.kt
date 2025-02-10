package com.daniebeler.pfpixelix.utils

import coil3.PlatformContext
import com.daniebeler.pfpixelix.ui.composables.settings.icon_selection.IconWithName
import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.Settings
import kotlinx.cinterop.ExperimentalForeignApi
import okio.Path
import okio.Path.Companion.toPath
import platform.Foundation.NSBundle
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDefaults
import platform.Foundation.NSUserDomainMask

private data class IosUri(val uri: String) : KmpUri() {
    override fun toString(): String = uri
}

actual abstract class KmpUri {
    actual abstract override fun toString(): String
}

actual abstract class KmpContext

actual val KmpContext.dataStoreDir get() = appDocDir().resolve("dataStore")
actual val KmpContext.imageCacheDir get() = appDocDir().resolve("imageCache")
actual val KmpContext.coilContext get() = PlatformContext.INSTANCE

@OptIn(ExperimentalForeignApi::class)
private fun appDocDir() = NSFileManager.defaultManager.URLForDirectory(
    directory = NSDocumentDirectory,
    inDomain = NSUserDomainMask,
    appropriateForURL = null,
    create = false,
    error = null,
)!!.path!!.toPath()

actual fun KmpContext.openUrlInApp(url: String) {
}

actual fun KmpContext.openUrlInBrowser(url: String) {
}

actual val KmpContext.pref: Settings
    get() = NSUserDefaultsSettings(NSUserDefaults())
actual val KmpContext.appVersionName: String
    get() = NSBundle.mainBundle.infoDictionary?.get("CFBundleShortVersionString").toString()

actual fun KmpContext.setDefaultNightMode(mode: Int) {
}

actual fun KmpContext.getCacheSizeInBytes(): Long {
    return 0L //TODO("Not yet implemented")
}

actual fun KmpContext.cleanCache() {
}

actual fun KmpContext.getAppIcons(): List<IconWithName> {
    return emptyList() // TODO("Not yet implemented")
}

actual fun KmpContext.enableCustomIcon(iconWithName: IconWithName) {
}

actual fun KmpContext.disableCustomIcon() {
}

actual fun String.toKmpUri(): KmpUri = IosUri(this)

actual val EmptyKmpUri: KmpUri = IosUri("")
actual fun KmpContext.pinWidget() {
}

actual fun isAbleToDownloadImage(): Boolean {
    return false //TODO("Not yet implemented")
}