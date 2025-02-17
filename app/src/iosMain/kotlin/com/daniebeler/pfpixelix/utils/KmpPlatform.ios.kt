package com.daniebeler.pfpixelix.utils

import coil3.PlatformContext
import com.daniebeler.pfpixelix.ui.composables.settings.icon_selection.IconWithName
import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.Settings
import io.github.vinceglb.filekit.core.PlatformFile
import kotlinx.cinterop.ExperimentalForeignApi
import okio.Path
import okio.Path.Companion.toPath
import platform.Foundation.NSBundle
import platform.Foundation.NSDictionary
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDefaults
import platform.Foundation.NSUserDomainMask
import platform.Foundation.fileSize
import platform.UIKit.UIApplication
import platform.UIKit.UIViewController

private data class IosUri(override val url: NSURL) : KmpUri() {
    override fun toString(): String = url.toString()
}

actual abstract class KmpUri {
    abstract val url: NSURL
    actual abstract override fun toString(): String
}

actual abstract class KmpContext {
    abstract val viewController: UIViewController
}

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
    openUrlInBrowser(url)
}

actual fun KmpContext.openUrlInBrowser(url: String) {
    UIApplication.sharedApplication.openURL(NSURL(string = url))
}

actual val KmpContext.pref: Settings
    get() = NSUserDefaultsSettings(NSUserDefaults())
actual val KmpContext.appVersionName: String
    get() = NSBundle.mainBundle.infoDictionary?.get("CFBundleShortVersionString").toString()

actual fun KmpContext.setDefaultNightMode(mode: Int) {
}

@OptIn(ExperimentalForeignApi::class)
actual fun KmpContext.getCacheSizeInBytes(): Long {
    val fm = NSFileManager.defaultManager()
    val cacheDir = imageCacheDir
    val files = fm.subpathsOfDirectoryAtPath(cacheDir.toString(), null).orEmpty()
    var result = 0uL
    files.map { file ->
        val dict = fm.fileAttributesAtPath(
            cacheDir.resolve(file.toString()).toString(),
            true
        ) as NSDictionary
        result += dict.fileSize()
    }
    return result.toLong()
}

@OptIn(ExperimentalForeignApi::class)
actual fun KmpContext.cleanCache() {
    val fm = NSFileManager.defaultManager()
    fm.removeItemAtPath(imageCacheDir.toString(), null)
}

actual fun KmpContext.getAppIcons(): List<IconWithName> {
    return emptyList() // TODO("Not yet implemented")
}

actual fun KmpContext.enableCustomIcon(iconWithName: IconWithName) {
}

actual fun KmpContext.disableCustomIcon() {
}

actual fun String.toKmpUri(): KmpUri = IosUri(NSURL(string = this))
actual fun PlatformFile.toKmpUri(): KmpUri = IosUri(nsUrl)
actual val EmptyKmpUri: KmpUri = IosUri(NSURL(string = ""))
actual fun KmpContext.pinWidget() {
}

actual fun isAbleToDownloadImage(): Boolean {
    return false //TODO("Not yet implemented")
}

actual fun KmpUri.getPlatformUriObject(): Any = url