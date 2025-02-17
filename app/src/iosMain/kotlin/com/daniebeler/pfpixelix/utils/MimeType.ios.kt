package com.daniebeler.pfpixelix.utils

import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreFoundation.CFRelease
import platform.CoreFoundation.CFStringRef
import platform.CoreServices.UTTypeCopyPreferredTagWithClass
import platform.CoreServices.UTTypeCreatePreferredIdentifierForTag
import platform.CoreServices.kUTTagClassFilenameExtension
import platform.CoreServices.kUTTagClassMIMEType
import platform.Foundation.CFBridgingRelease
import platform.Foundation.CFBridgingRetain
import platform.Foundation.NSString

actual object MimeType {
    @OptIn(ExperimentalForeignApi::class)
    actual fun getMimeType(
        uri: KmpUri,
        context: KmpContext
    ): String? {
        val fileExtension = uri.url.pathExtension()
        val fileExtensionRef = CFBridgingRetain(fileExtension as NSString) as CFStringRef
        val uti = UTTypeCreatePreferredIdentifierForTag(kUTTagClassFilenameExtension, fileExtensionRef, null)
        CFRelease(fileExtensionRef)
        val  mimeType = UTTypeCopyPreferredTagWithClass(uti, kUTTagClassMIMEType)
        CFRelease(uti)
        return CFBridgingRelease(mimeType) as String
    }
}