package com.daniebeler.pfpixelix.utils

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

object GetFile {
    fun getFile(uri: Uri, context: Context): File? {
        // Retrieves a file from URI, copies it and returns the copied file
        val fileName = getFileName(uri)
        if (!fileName.isNullOrEmpty()) {
            val copyFile = File(context.getExternalFilesDir("Pixelix"), fileName)
            copy(context, uri, copyFile)
            return File(copyFile.absolutePath)
        }
        return null
    }

    private fun getFileName(uri: Uri): String? {
        // Extract file name from given Uri
        val path = uri.path ?: return null
        val lastSlashIndex = path.lastIndexOf("/")
        return if (lastSlashIndex != -1) path.substring(lastSlashIndex + 1) else null
    }

    private fun copy(context: Context, uri: Uri, file: File) {
        // copy the contents of a file from the uri to the destination file
        try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            inputStream?.use { input ->
                val outputStream: OutputStream = FileOutputStream(file)
                outputStream.use { output ->
                    input.buffered().copyTo(output.buffered())
                }
            }
        }
        catch (e: IOException) {
            e.printStackTrace()
        }
    }
}