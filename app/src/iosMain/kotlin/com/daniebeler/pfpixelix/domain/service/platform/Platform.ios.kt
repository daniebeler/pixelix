package com.daniebeler.pfpixelix.domain.service.platform

import com.daniebeler.pfpixelix.utils.KmpContext
import com.daniebeler.pfpixelix.utils.KmpUri
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.get
import kotlinx.cinterop.refTo
import kotlinx.cinterop.usePinned
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject
import platform.CoreFoundation.CFDataGetBytePtr
import platform.CoreFoundation.CFDataGetLength
import platform.CoreFoundation.CFDictionaryAddValue
import platform.CoreFoundation.CFDictionaryCreateMutable
import platform.CoreFoundation.CFRelease
import platform.CoreFoundation.CFStringRef
import platform.CoreFoundation.CFURLRef
import platform.CoreGraphics.CGDataProviderCopyData
import platform.CoreGraphics.CGImageGetDataProvider
import platform.CoreServices.UTTypeCopyPreferredTagWithClass
import platform.CoreServices.UTTypeCreatePreferredIdentifierForTag
import platform.CoreServices.kUTTagClassFilenameExtension
import platform.CoreServices.kUTTagClassMIMEType
import platform.Foundation.CFBridgingRelease
import platform.Foundation.CFBridgingRetain
import platform.Foundation.NSData
import platform.Foundation.NSFileManager
import platform.Foundation.NSFileSize
import platform.Foundation.NSNumber
import platform.Foundation.NSString
import platform.Foundation.dataWithContentsOfURL
import platform.ImageIO.CGImageSourceCreateThumbnailAtIndex
import platform.ImageIO.CGImageSourceCreateWithURL
import platform.ImageIO.kCGImageSourceCreateThumbnailFromImageAlways
import platform.ImageIO.kCGImageSourceCreateThumbnailWithTransform
import platform.ImageIO.kCGImageSourceThumbnailMaxPixelSize
import platform.posix.memcpy

@Inject
actual class Platform actual constructor(
    private val context: KmpContext
) {
    actual fun getPlatformFile(uri: KmpUri): PlatformFile? {
        val f = IosFile(uri)
        return if (f.getName() != "IosFile:unknown") f else null
    }
}

@OptIn(ExperimentalForeignApi::class)
private class IosFile(
    private val uri: KmpUri
): PlatformFile {
    override fun getName(): String {
        return uri.url.lastPathComponent() ?: "IosFile:unknown"
    }

    override fun getSize(): Long {
        val path = uri.url.path ?: return 0L
        val fm = NSFileManager.defaultManager
        val attr = fm.attributesOfItemAtPath(path, null) ?: return 0L
        return attr.getValue(NSFileSize) as Long
    }

    override fun getMimeType(): String {
        val fileExtension = uri.url.pathExtension()
        val fileExtensionRef = CFBridgingRetain(fileExtension as NSString) as CFStringRef
        val uti = UTTypeCreatePreferredIdentifierForTag(kUTTagClassFilenameExtension, fileExtensionRef, null)
        CFRelease(fileExtensionRef)
        val  mimeType = UTTypeCopyPreferredTagWithClass(uti, kUTTagClassMIMEType)
        CFRelease(uti)
        return CFBridgingRelease(mimeType) as String
    }

    override suspend fun readBytes(): ByteArray = withContext(Dispatchers.IO) {
        val data = NSData.dataWithContentsOfURL(uri.url)!!
        ByteArray(data.length.toInt()).apply {
            data.usePinned {
                memcpy(refTo(0), data.bytes, data.length)
            }
        }
    }

    override suspend fun getThumbnail(): ByteArray? = withContext(Dispatchers.IO) {
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