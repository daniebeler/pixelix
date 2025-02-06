package com.daniebeler.pfpixelix.domain.usecase

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import coil3.ImageLoader
import coil3.request.ImageRequest
import coil3.request.SuccessResult
import coil3.request.allowHardware
import coil3.toBitmap
import com.daniebeler.pfpixelix.utils.KmpContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import me.tatarka.inject.annotations.Inject
import java.io.File
import java.io.FileNotFoundException

@Inject
actual class DownloadImageUseCase {
    actual operator fun invoke(
        name: String?,
        url: String,
        context: KmpContext
    ) {
        var uri: Uri? = null
        val saveImageRoutine = CoroutineScope(Dispatchers.Default).launch {

            val bitmap: Bitmap? = urlToBitmap(url, context)
            if (bitmap == null) {
                cancel("an error occured when downloading the image")
                return@launch
            }

            println(bitmap.toString())

            uri = saveImageToMediaStore(context, generateUniqueName(name, false, context), bitmap!!)
            if (uri == null) {
                cancel("an error occured when saving the image")
                return@launch
            }
        }

        saveImageRoutine.invokeOnCompletion { throwable ->
            CoroutineScope(Dispatchers.Main).launch {
                uri?.let {
                    Toast.makeText(context, "Stored at: " + uri.toString(), Toast.LENGTH_LONG)
                        .show()
                } ?: throwable?.let {
                    Toast.makeText(
                        context, "an error occurred downloading the image", Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun generateUniqueName(
        imageName: String?, returnFullPath: Boolean, context: KmpContext
    ): String {

        val filename = "${imageName}_${Clock.System.now().epochSeconds}"

        if (returnFullPath) {
            val directory: File = context.getDir("zest", Context.MODE_PRIVATE)
            return "$directory/$filename"
        } else {
            return filename
        }
    }

    fun saveImage(name: String?, url: String, context: KmpContext) {

    }

    private suspend fun urlToBitmap(
        imageURL: String,
        context: KmpContext,
    ): Bitmap? {
        val loader = ImageLoader(context)
        val request = ImageRequest.Builder(context).data(imageURL).allowHardware(false).build()
        val result = loader.execute(request)
        if (result is SuccessResult) {
            return result.image.toBitmap()
        }
        return null
    }

    private fun saveImageToMediaStore(context: KmpContext, displayName: String, bitmap: Bitmap): Uri? {
        val imageCollections = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

        val imageDetails = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, displayName)
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.Images.Media.IS_PENDING, 1)
            }
        }

        val resolver = context.applicationContext.contentResolver
        val imageContentUri = resolver.insert(imageCollections, imageDetails) ?: return null

        return try {
            resolver.openOutputStream(imageContentUri, "w").use { os ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, os!!)
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                imageDetails.clear()
                imageDetails.put(MediaStore.Images.Media.IS_PENDING, 0)
                resolver.update(imageContentUri, imageDetails, null, null)
            }

            imageContentUri
        } catch (e: FileNotFoundException) {
            // Some legacy devices won't create directory for the Uri if dir not exist, resulting in
            // a FileNotFoundException. To resolve this issue, we should use the File API to save the
            // image, which allows us to create the directory ourselves.
            null
        }
    }
}