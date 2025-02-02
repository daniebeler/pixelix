package com.daniebeler.pfpixelix.ui.composables.settings.preferences

import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniebeler.pfpixelix.domain.usecase.GetActiveAppIconUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetOwnInstanceDomainUseCase
import com.daniebeler.pfpixelix.domain.usecase.LogoutUseCase
import com.daniebeler.pfpixelix.domain.usecase.OpenExternalUrlUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreferencesViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase,
    private val getOwnInstanceDomainUseCase: GetOwnInstanceDomainUseCase,
    private val openExternalUrlUseCase: OpenExternalUrlUseCase,
    private val getActiveAppIconUseCase: GetActiveAppIconUseCase
) : ViewModel() {

    var appIcon by mutableStateOf<ImageBitmap?>(null)

    var cacheSize by mutableStateOf("")

    var versionName by mutableStateOf("")

    fun getAppIcon(context: Context) {
        appIcon = getActiveAppIconUseCase(context)
    }

    fun getVersionName(context: Context) {
        try {
            versionName =
                context.packageManager.getPackageInfo(context.packageName, 0).versionName ?: ""
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
    }

    fun logout() {
        viewModelScope.launch {
            logoutUseCase()
        }
    }

    fun openMoreSettingsPage() {
        viewModelScope.launch {
            val domain = getOwnInstanceDomainUseCase()
            val moreSettingUrl = "https://$domain/settings/home"
            openExternalUrlUseCase(moreSettingUrl)
        }
    }

    fun openRepostSettings() {
        viewModelScope.launch {
            val domain = getOwnInstanceDomainUseCase()
            val moreSettingUrl = "https://$domain/settings/timeline"
            openExternalUrlUseCase(moreSettingUrl)
        }
    }
}