package com.daniebeler.pfpixelix.utils

expect object MimeType {
    fun getMimeType(uri: KmpUri, context: KmpContext): String?
}