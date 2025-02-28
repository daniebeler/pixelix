package com.daniebeler.pfpixelix.ui.composables.settings.about_pixelix

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import com.daniebeler.pfpixelix.domain.service.platform.Platform
import com.daniebeler.pfpixelix.utils.KmpContext
import me.tatarka.inject.annotations.Inject

class AboutPixelixViewModel @Inject constructor(
    private val platform: Platform
) : ViewModel() {
    private val iconManager = platform.getAppIconManager()

    var versionName by mutableStateOf("")

    var appIcon by mutableStateOf<ImageBitmap?>(null)

    fun getVersionName(context: KmpContext) {
        versionName = platform.getAppVersion()
    }

    fun getAppIcon(context: KmpContext) {
        appIcon = iconManager.getCurrentIcon()
    }

    fun rateApp(context: KmpContext) {
        platform.openUrl(
             "https://play.google.com/store/apps/details?id=com.daniebeler.pfpixelix"
        )
    }

    fun openUrl(url: String, context: KmpContext) {
        platform.openUrl(url)
    }
}