package com.daniebeler.pfpixelix.utils

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import coil3.PlatformContext
import io.github.vinceglb.filekit.core.PlatformFile
import okio.Path
import okio.Path.Companion.toPath
import java.io.File

actual typealias KmpUri = Uri
actual val EmptyKmpUri: KmpUri = Uri.EMPTY
actual fun KmpUri.getPlatformUriObject(): Any = this
actual fun String.toKmpUri(): KmpUri = this.toUri()
actual fun PlatformFile.toKmpUri(): KmpUri = this.uri

actual typealias KmpContext = Context
actual val KmpContext.coilContext: PlatformContext get() = this
actual val KmpContext.dataStoreDir: Path
    get() = File(applicationContext.filesDir, "datastore").path.toPath()
actual val KmpContext.imageCacheDir: Path
    get() = cacheDir.path.toPath().resolve("image_cache")
