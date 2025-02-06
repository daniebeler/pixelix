package com.daniebeler.pfpixelix.utils

import coil3.PlatformContext
import com.daniebeler.pfpixelix.ui.composables.settings.icon_selection.IconWithName
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
actual val KmpContext.appVersionName: String
    get() = TODO("Not yet implemented")

actual fun KmpContext.setDefaultNightMode(mode: Int) {
}

actual fun KmpContext.openLoginScreen(isAbleToGotBack: Boolean) {
}

actual fun KmpContext.getCacheSizeInBytes(): Long {
    TODO("Not yet implemented")
}

actual fun KmpContext.cleanCache() {
}

actual fun KmpContext.getAppIcons(): List<IconWithName> {
    TODO("Not yet implemented")
}

actual fun KmpContext.enableCustomIcon(iconWithName: IconWithName) {
}

actual fun KmpContext.disableCustomIcon() {
}

actual fun String.toKmpUri(): KmpUri {
    TODO("Not yet implemented")
}

actual val EmptyKmpUri: KmpUri = TODO("Not yet implemented")
actual fun KmpContext.pinWidget() {
}

actual fun isAbleToDownloadImage(): Boolean {
    TODO("Not yet implemented")
}