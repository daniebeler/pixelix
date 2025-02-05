package com.daniebeler.pfpixelix.utils

import android.content.ContentResolver
import android.net.Uri
import android.webkit.MimeTypeMap

actual object MimeType {
    actual fun getMimeType(uri: KmpUri, context: KmpContext): String? {
        return if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
            context.contentResolver.getType(uri)
        } else {
            val fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString())
            MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension.lowercase())
        }
    }
}