package com.daniebeler.pfpixelix.domain.service.platform

import com.daniebeler.pfpixelix.utils.KmpContext
import com.daniebeler.pfpixelix.utils.KmpUri
import me.tatarka.inject.annotations.Inject

@Inject
expect class Platform(context: KmpContext) {
    fun getPlatformFile(uri: KmpUri): PlatformFile?
}

interface PlatformFile {
    fun getName(): String
    fun getSize(): Long
    fun getMimeType(): String
    suspend fun readBytes(): ByteArray
    suspend fun getThumbnail(): ByteArray?
}