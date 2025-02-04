package com.daniebeler.pfpixelix.utils

import okio.Path

expect abstract class KmpUri {
    abstract override fun toString(): String
}

expect abstract class KmpContext

expect val KmpContext.dataStoreDir: Path
