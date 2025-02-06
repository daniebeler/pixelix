package com.daniebeler.pfpixelix.ui.composables.settings.about_pixelix

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import com.daniebeler.pfpixelix.domain.usecase.GetActiveAppIconUseCase
import com.daniebeler.pfpixelix.domain.usecase.OpenExternalUrlUseCase
import com.daniebeler.pfpixelix.utils.KmpContext
import com.daniebeler.pfpixelix.utils.appVersionName
import me.tatarka.inject.annotations.Inject

class AboutPixelixViewModel @Inject constructor(
    private val openExternalUrlUseCase: OpenExternalUrlUseCase,
    private val getActiveAppIconUseCase: GetActiveAppIconUseCase
) : ViewModel() {

    var versionName by mutableStateOf("")

    var appIcon by mutableStateOf<ImageBitmap?>(null)

    fun getVersionName(context: KmpContext) {
        versionName = context.appVersionName
    }

    fun getAppIcon(context: KmpContext){
        appIcon = getActiveAppIconUseCase(context)
    }

    fun rateApp(context: KmpContext) {
        openExternalUrlUseCase(
             "https://play.google.com/store/apps/details?id=com.daniebeler.pfpixelix", context
        )
    }

    fun openUrl(url: String, context: KmpContext) {
        openExternalUrlUseCase(
            url,context
        )
    }
}