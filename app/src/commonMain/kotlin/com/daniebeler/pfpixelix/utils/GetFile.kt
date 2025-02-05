package com.daniebeler.pfpixelix.utils

expect object GetFile {
    fun getFileName(uri: KmpUri, context: KmpContext): String?
    fun getFileSize(uri: KmpUri, context: KmpContext): Long?
}