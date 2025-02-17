package com.daniebeler.pfpixelix.utils

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSFileManager
import platform.Foundation.NSFileSize

actual object GetFile {
    actual fun getFileName(
        uri: KmpUri,
        context: KmpContext
    ): String? {
        return uri.url.lastPathComponent()
    }

    @OptIn(ExperimentalForeignApi::class)
    actual fun getFileSize(
        uri: KmpUri,
        context: KmpContext
    ): Long? {
        val path = uri.url.path ?: return null
        val fm = NSFileManager.defaultManager
        val attr = fm.attributesOfItemAtPath(path, null) ?: return null
        return attr.getValue(NSFileSize) as Long
    }
}