package com.daniebeler.pfpixelix.utils

import coil3.PlatformContext
import okio.Path

expect abstract class KmpUri {
    abstract override fun toString(): String
}

expect abstract class KmpContext

expect val KmpContext.dataStoreDir: Path
expect val KmpContext.imageCacheDir: Path

expect val KmpContext.coilContext: PlatformContext
