package com.daniebeler.pfpixelix.utils

actual class KmpMediaFile actual constructor(
    uri: KmpUri,
    context: KmpContext
) {
    actual val uri: KmpUri
        get() = TODO("Not yet implemented")
    actual val context: KmpContext
        get() = TODO("Not yet implemented")

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