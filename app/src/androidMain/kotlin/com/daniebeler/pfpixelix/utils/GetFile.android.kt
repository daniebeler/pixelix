package com.daniebeler.pfpixelix.utils

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns


actual object GetFile {
    actual fun getFileName(uri: Uri, context: Context): String? = when(uri.scheme) {
        "file" -> uri.pathSegments.last().substringBeforeLast('.')
        "content" -> context.contentResolver.query(
            uri, null, null, null, null
        )?.use {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            it.moveToFirst()
            it.getString(nameIndex)
        }
        else -> null
    }

    actual fun getFileSize(uri: Uri, context: Context): Long? = when(uri.scheme) {
        "file" -> context.contentResolver.openFileDescriptor(uri, "r")?.use { it.statSize }
        "content" -> context.contentResolver.query(
            uri, null, null, null, null
        )?.use {
            val nameIndex = it.getColumnIndex(OpenableColumns.SIZE)
            it.moveToFirst()
            it.getLong(nameIndex)
        }
        else -> null
    }
}