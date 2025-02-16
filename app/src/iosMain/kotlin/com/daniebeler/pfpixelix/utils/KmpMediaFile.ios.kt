package com.daniebeler.pfpixelix.utils

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.get
import kotlinx.cinterop.refTo
import kotlinx.cinterop.usePinned
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import platform.CoreFoundation.CFDataGetBytePtr
import platform.CoreFoundation.CFDataGetLength
import platform.CoreFoundation.CFDictionaryAddValue
import platform.CoreFoundation.CFDictionaryCreateMutable
import platform.CoreFoundation.CFRelease
import platform.CoreFoundation.CFURLRef
import platform.CoreGraphics.CGDataProviderCopyData
import platform.CoreGraphics.CGImageGetDataProvider
import platform.Foundation.CFBridgingRetain
import platform.Foundation.NSData
import platform.Foundation.NSNumber
import platform.Foundation.dataWithContentsOfURL
import platform.ImageIO.CGImageSourceCreateThumbnailAtIndex
import platform.ImageIO.CGImageSourceCreateWithURL
import platform.ImageIO.kCGImageSourceCreateThumbnailFromImageAlways
import platform.ImageIO.kCGImageSourceCreateThumbnailWithTransform
import platform.ImageIO.kCGImageSourceThumbnailMaxPixelSize
import platform.posix.memcpy

@OptIn(ExperimentalForeignApi::class)
actual class KmpMediaFile actual constructor(
    actual val uri: KmpUri,
    actual val context: KmpContext
) {
    private val fileName = GetFile.getFileName(uri, context) ?: error("file '$uri' not found")

    actual fun getMimeType(): String = MimeType.getMimeType(uri, context) ?: "image/*"

    actual suspend fun getBytes(): ByteArray = withContext(Dispatchers.IO) {
        val data = NSData.dataWithContentsOfURL(uri.url)!!
        ByteArray(data.length.toInt()).apply {
            data.usePinned {
                memcpy(refTo(0), data.bytes, data.length)
            }
        }
    }

    actual fun getName(): String = fileName

    actual suspend fun getThumbnail(): ByteArray? = withContext(Dispatchers.IO) {
        val urlRef = CFBridgingRetain(uri.url) as CFURLRef
        val imageSource = CGImageSourceCreateWithURL(urlRef, null)!!
        val thumbnailOptions = CFDictionaryCreateMutable(
            null,
            3,
            null,
            null
        ).apply {
            CFDictionaryAddValue(
                this,
                kCGImageSourceCreateThumbnailWithTransform,
                CFBridgingRetain(NSNumber(bool = true))
            )
            CFDictionaryAddValue(
                this,
                kCGImageSourceCreateThumbnailFromImageAlways,
                CFBridgingRetain(NSNumber(bool = true))
            )
            CFDictionaryAddValue(
                this,
                kCGImageSourceThumbnailMaxPixelSize,
                CFBridgingRetain(NSNumber(512))
            )
        }

        val thumbnailSource = CGImageSourceCreateThumbnailAtIndex(
            imageSource,
            0u,
            thumbnailOptions
        )

        val data = CGDataProviderCopyData(CGImageGetDataProvider(thumbnailSource))
        val bytePointer = CFDataGetBytePtr(data)!!
        val length = CFDataGetLength(data)

        val byteArray = ByteArray(length.toInt()) { index ->
            bytePointer[index].toByte()
        }

        CFRelease(urlRef)
        CFRelease(data)
        CFRelease(thumbnailSource)

        byteArray
    }
}