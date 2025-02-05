package com.daniebeler.pfpixelix.utils

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns


actual object GetFile {
    actual fun getFileName(uri: Uri, context: Context): String? = context.contentResolver.query(
        uri, null, null, null, null
    )?.use {
        val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        it.moveToFirst()
        it.getString(nameIndex)
    }

    actual fun getFileSize(uri: Uri, context: Context): Long? = context.contentResolver.query(
        uri, null, null, null, null
    )?.use {
        val sizeIndex = it.getColumnIndex(OpenableColumns.SIZE)
        it.moveToFirst()
        it.getLong(sizeIndex)
    }
}