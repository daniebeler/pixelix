package com.daniebeler.pfpixelix.domain.service.platform

import androidx.compose.ui.graphics.ImageBitmap
import com.daniebeler.pfpixelix.domain.service.preferences.UserPreferences
import com.daniebeler.pfpixelix.ui.composables.settings.icon_selection.IconWithName
import com.daniebeler.pfpixelix.utils.KmpContext
import com.daniebeler.pfpixelix.utils.KmpUri
import com.daniebeler.pfpixelix.utils.imageCacheDir
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
import platform.Foundation.NSBundle
import platform.Foundation.NSData
import platform.Foundation.NSDictionary
import platform.Foundation.NSFileManager
import platform.Foundation.NSFileSize
import platform.Foundation.NSNumber
import platform.Foundation.NSString
import platform.Foundation.NSURL
import platform.Foundation.dataWithContentsOfURL
import platform.Foundation.fileSize
import platform.ImageIO.CGImageSourceCreateThumbnailAtIndex
import platform.ImageIO.CGImageSourceCreateWithURL
import platform.ImageIO.kCGImageSourceCreateThumbnailFromImageAlways
import platform.ImageIO.kCGImageSourceCreateThumbnailWithTransform
import platform.ImageIO.kCGImageSourceThumbnailMaxPixelSize
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication
import platform.posix.memcpy

@Inject
actual class Platform actual constructor(
    private val context: KmpContext,
    private val prefs: UserPreferences
) {
    actual fun getPlatformFile(uri: KmpUri): PlatformFile? {
        val f = IosFile(uri)
        return if (f.getName() != "IosFile:unknown") f else null
    }

    actual fun getAppIconManager(): AppIconManager {
        return IosAppIconManager()
    }

    actual fun openUrl(url: String) {
        UIApplication.sharedApplication.openURL(NSURL(string = url))
    }

    actual fun shareText(text: String) {
        val vc = UIActivityViewController(listOf(text), null)
        context.viewController.presentViewController(vc, true, null)
    }

    actual fun getAppVersion(): String {
        return NSBundle.mainBundle.infoDictionary?.get("CFBundleShortVersionString").toString()
    }
    actual fun pinWidget() {}

    actual fun downloadImageToGallery(name: String?, url: String) {}

    @OptIn(ExperimentalForeignApi::class)
    actual fun getCacheSizeInBytes(): Long {
        val fm = NSFileManager.defaultManager()
        val cacheDir = context.imageCacheDir
        val files = fm.subpathsOfDirectoryAtPath(cacheDir.toString(), null).orEmpty()
        var result = 0uL
        files.map { file ->
            val dict = fm.fileAttributesAtPath(
                cacheDir.resolve(file.toString()).toString(),
                true
            ) as NSDictionary
            result += dict.fileSize()
        }
        return result.toLong()
    }
    @OptIn(ExperimentalForeignApi::class)
    actual fun cleanCache() {
        val fm = NSFileManager.defaultManager()
        fm.removeItemAtPath(context.imageCacheDir.toString(), null)
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

private class IosAppIconManager : AppIconManager {
    override fun getIcons(): List<IconWithName> {
        return emptyList()
    }

    override fun getCurrentIcon(): ImageBitmap? {
        return null
    }

    override fun enableCustomIcon(iconWithName: IconWithName) {
    }

    override fun disableCustomIcon() {
    }

}