package com.daniebeler.pfpixelix.utils

actual class KmpMediaFile actual constructor(
    actual val uri: KmpUri,
    actual val context: KmpContext
) {
    actual fun getMimeType(): String {
        TODO("Not yet implemented")
    }

    actual suspend fun getBytes(): ByteArray {
        TODO("Not yet implemented")
    }

    actual fun getName(): String {
        TODO("Not yet implemented")
    }

    actual suspend fun getThumbnail(): ByteArray? {
        TODO("Not yet implemented")
    }

}