package com.daniebeler.pfpixelix.utils

import android.content.ContentResolver
import android.net.Uri
import android.webkit.MimeTypeMap

object MimeType {
    fun getMimeType(uri: Uri, contentResolver: ContentResolver): String? {
        return if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
            contentResolver.getType(uri)
        } else {
            val fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString())
            MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension.lowercase())
        }
    }
}