package com.daniebeler.pfpixelix.ui.composables.settings.about_pixelix

import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import com.daniebeler.pfpixelix.domain.usecase.GetActiveAppIconUseCase
import com.daniebeler.pfpixelix.domain.usecase.OpenExternalUrlUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AboutPixelixViewModel @Inject constructor(
    private val openExternalUrlUseCase: OpenExternalUrlUseCase,
    private val getActiveAppIconUseCase: GetActiveAppIconUseCase
) : ViewModel() {

    var versionName by mutableStateOf("")

    var appIcon by mutableStateOf<ImageBitmap?>(null)

    fun getVersionName(context: Context) {
        try {
            versionName = context.packageManager.getPackageInfo(context.packageName, 0).versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
    }

    fun getAppIcon(context: Context){
        appIcon = getActiveAppIconUseCase(context)
    }

    fun rateApp(context: Context) {
        openExternalUrlUseCase(
            context, "https://play.google.com/store/apps/details?id=com.daniebeler.pfpixelix"
        )
    }

    fun openUrl(context: Context, url: String) {
        openExternalUrlUseCase(
            context, url
        )
    }
}