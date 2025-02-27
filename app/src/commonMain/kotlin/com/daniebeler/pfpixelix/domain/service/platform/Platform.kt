package com.daniebeler.pfpixelix.domain.service.platform

import androidx.compose.ui.graphics.ImageBitmap
import com.daniebeler.pfpixelix.ui.composables.settings.icon_selection.IconWithName
import com.daniebeler.pfpixelix.utils.KmpContext
import com.daniebeler.pfpixelix.utils.KmpUri
import me.tatarka.inject.annotations.Inject

@Inject
expect class Platform(context: KmpContext) {
    fun getPlatformFile(uri: KmpUri): PlatformFile?
    fun getAppIconManager(): AppIconManager
}

interface PlatformFile {
    fun getName(): String
    fun getSize(): Long
    fun getMimeType(): String
    suspend fun readBytes(): ByteArray
    suspend fun getThumbnail(): ByteArray?
}

interface AppIconManager {
    fun getIcons(): List<IconWithName>
    fun getCurrentIcon(): ImageBitmap?
    fun enableCustomIcon(iconWithName: IconWithName)
    fun disableCustomIcon()
}
