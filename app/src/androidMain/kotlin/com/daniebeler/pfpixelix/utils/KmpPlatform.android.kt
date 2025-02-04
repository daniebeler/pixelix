package com.daniebeler.pfpixelix.utils

import android.content.Context
import android.net.Uri
import okio.Path
import okio.Path.Companion.toPath
import java.io.File

actual typealias KmpUri = Uri
actual typealias KmpContext = Context

actual val KmpContext.dataStoreDir: Path
    get() = File(applicationContext.filesDir, "datastore").path.toPath()