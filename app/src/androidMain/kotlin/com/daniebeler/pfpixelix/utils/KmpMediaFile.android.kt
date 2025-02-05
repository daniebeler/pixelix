package com.daniebeler.pfpixelix.utils

import android.graphics.Bitmap
import coil3.SingletonImageLoader
import coil3.request.ImageRequest
import coil3.toBitmap
import coil3.video.videoFrameMillis
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

actual class KmpMediaFile actual constructor(
    actual val uri: KmpUri,
    actual val context: KmpContext
) {
    private val file = GetFile.getFile(uri, context) ?: error("file '$uri' not found")

    actual fun getMimeType(): String = MimeType.getMimeType(uri, context.contentResolver) ?: "image/*"

    actual suspend fun getBytes(): ByteArray = withContext(Dispatchers.IO) {
        context.contentResolver.openInputStream(uri)!!.readBytes()
    }

    actual fun getName(): String = file.name

    actual suspend fun getThumbnail(): ByteArray? = withContext(Dispatchers.IO) {
        val bm = try {
            val req = ImageRequest.Builder(context).data(uri).videoFrameMillis(0).build()
            val img = SingletonImageLoader.get(context).execute(req)
            img.image?.toBitmap()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } ?: return@withContext null

        val stream = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.PNG, 100, stream)
        stream.toByteArray()
    }
}