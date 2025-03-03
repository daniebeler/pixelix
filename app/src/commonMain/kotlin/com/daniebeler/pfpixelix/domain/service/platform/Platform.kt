package com.daniebeler.pfpixelix.domain.service.platform

import com.daniebeler.pfpixelix.domain.service.preferences.UserPreferences
import com.daniebeler.pfpixelix.utils.KmpContext
import com.daniebeler.pfpixelix.utils.KmpUri
import me.tatarka.inject.annotations.Inject
import org.jetbrains.compose.resources.DrawableResource

@Inject
expect class Platform(
    context: KmpContext,
    prefs: UserPreferences
) {
    fun getPlatformFile(uri: KmpUri): PlatformFile?
    fun getAppIconManager(): AppIconManager
    fun openUrl(url: String)
    fun shareText(text: String)
    fun getAppVersion(): String
    fun pinWidget()

    fun downloadImageToGallery(name: String?, url: String)

    fun getCacheSizeInBytes(): Long
    fun cleanCache()
}

interface PlatformFile {
    fun getName(): String
    fun getSize(): Long
    fun getMimeType(): String
    suspend fun readBytes(): ByteArray
    suspend fun getThumbnail(): ByteArray?
}

interface AppIconManager {
    fun getCurrentIcon(): DrawableResource
    fun setCustomIcon(icon: DrawableResource)
}
