package com.daniebeler.pfpixelix.domain.service.platform

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.OpenableColumns
import android.webkit.MimeTypeMap
import co.touchlab.kermit.Logger
import coil3.SingletonImageLoader
import coil3.request.ImageRequest
import coil3.toBitmap
import coil3.video.videoFrameMillis
import com.daniebeler.pfpixelix.utils.KmpContext
import com.daniebeler.pfpixelix.utils.KmpUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject
import java.io.ByteArrayOutputStream

@Inject
actual class Platform actual constructor(
    private val context: KmpContext
) {
    actual fun getPlatformFile(uri: KmpUri): PlatformFile? {
        val f = AndroidFile(uri, context)
        return if (f.getName() != "AndroidFile:unknown") f else null
    }
}

private class AndroidFile(
    private val uri: Uri,
    private val context: Context
) : PlatformFile {
    override fun getName(): String = when (uri.scheme) {
        ContentResolver.SCHEME_FILE -> uri.pathSegments.last().substringBeforeLast('.')
        ContentResolver.SCHEME_CONTENT -> context.contentResolver.query(
            uri, null, null, null, null
        )?.use {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            it.moveToFirst()
            it.getString(nameIndex)
        }

        else -> null
    } ?: "AndroidFile:unknown"

    override fun getSize(): Long = when (uri.scheme) {
        ContentResolver.SCHEME_FILE -> context.contentResolver.openFileDescriptor(uri, "r")
            ?.use { it.statSize }

        ContentResolver.SCHEME_CONTENT -> context.contentResolver.query(
            uri, null, null, null, null
        )?.use {
            val nameIndex = it.getColumnIndex(OpenableColumns.SIZE)
            it.moveToFirst()
            it.getLong(nameIndex)
        }

        else -> null
    } ?: 0L

    override fun getMimeType(): String = when (uri.scheme) {
        ContentResolver.SCHEME_FILE -> {
            val fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString())
            MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension.lowercase())
        }

        ContentResolver.SCHEME_CONTENT -> {
            context.contentResolver.getType(uri)
        }

        else -> null
    } ?: "image/*"

    override suspend fun readBytes(): ByteArray = withContext(Dispatchers.IO) {
        context.contentResolver.openInputStream(uri)!!.readBytes()
    }

    override suspend fun getThumbnail(): ByteArray? = withContext(Dispatchers.IO) {
        val bm = try {
            val req = ImageRequest.Builder(context).data(uri).videoFrameMillis(0).build()
            val img = SingletonImageLoader.get(context).execute(req)
            img.image?.toBitmap()
        } catch (e: Exception) {
            Logger.e("AndroidFile.getThumbnail error", e)
            null
        } ?: return@withContext null

        val stream = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.PNG, 100, stream)
        stream.toByteArray()
    }
}