package com.daniebeler.pixelix.utils

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import java.io.File

class GetFile {
    fun getFile(uri: Uri, context: Context): File? {

        val contentResolver = context.contentResolver

        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                val filePath = it.getString(columnIndex)

                return File(filePath)
            }
        }
        return null
    }
}