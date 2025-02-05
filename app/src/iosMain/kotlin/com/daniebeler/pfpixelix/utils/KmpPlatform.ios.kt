package com.daniebeler.pfpixelix.utils

import okio.Path

actual abstract class KmpUri {
    actual abstract override fun toString(): String
}

actual abstract class KmpContext

actual val KmpContext.dataStoreDir: Path
    get() = TODO("Not yet implemented")