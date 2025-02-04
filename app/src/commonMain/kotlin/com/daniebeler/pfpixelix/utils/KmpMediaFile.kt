package com.daniebeler.pfpixelix.utils

expect class KmpMediaFile(
    uri: KmpUri,
    context: KmpContext
) {
    val uri: KmpUri
    val context: KmpContext

    fun getMimeType(): String
    suspend fun getBytes(): ByteArray
    fun getName(): String
    suspend fun getThumbnail(): ByteArray?
}