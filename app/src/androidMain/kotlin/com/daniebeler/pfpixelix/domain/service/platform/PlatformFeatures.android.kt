package com.daniebeler.pfpixelix.domain.service.platform

import android.os.Build

actual object PlatformFeatures {
    actual val notificationWidgets = true
    actual val inAppBrowser = true
    actual val downloadToGallery = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
}