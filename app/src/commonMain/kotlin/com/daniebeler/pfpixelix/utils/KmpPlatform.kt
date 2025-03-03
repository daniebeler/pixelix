package com.daniebeler.pfpixelix.utils

import androidx.compose.runtime.staticCompositionLocalOf
import coil3.PlatformContext
import io.github.vinceglb.filekit.core.PlatformFile
import okio.Path

expect abstract class KmpUri {
    abstract override fun toString(): String
}
expect val EmptyKmpUri: KmpUri
expect fun KmpUri.getPlatformUriObject(): Any
expect fun String.toKmpUri(): KmpUri
expect fun PlatformFile.toKmpUri(): KmpUri

expect abstract class KmpContext
val LocalKmpContext = staticCompositionLocalOf<KmpContext> { error("no KmpContext") }

expect val KmpContext.coilContext: PlatformContext
expect val KmpContext.dataStoreDir: Path
expect val KmpContext.imageCacheDir: Path
