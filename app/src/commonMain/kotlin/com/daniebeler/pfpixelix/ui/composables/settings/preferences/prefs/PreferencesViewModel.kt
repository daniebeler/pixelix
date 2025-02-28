package com.daniebeler.pfpixelix.ui.composables.settings.preferences

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniebeler.pfpixelix.domain.service.platform.Platform
import com.daniebeler.pfpixelix.domain.service.session.AuthService
import com.daniebeler.pfpixelix.domain.usecase.OpenExternalUrlUseCase
import com.daniebeler.pfpixelix.utils.KmpContext
import com.daniebeler.pfpixelix.utils.appVersionName
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject

class PreferencesViewModel @Inject constructor(
    private val authService: AuthService,
    private val openExternalUrlUseCase: OpenExternalUrlUseCase,
    private val platform: Platform
) : ViewModel() {
    private val iconManager = platform.getAppIconManager()

    var appIcon by mutableStateOf<ImageBitmap?>(null)
    var versionName by mutableStateOf("")

    fun getAppIcon(context: KmpContext) {
        appIcon = iconManager.getCurrentIcon()
    }

    fun getVersionName(context: KmpContext) {
        versionName = context.appVersionName
    }

    fun logout() {
        viewModelScope.launch {
            authService.deleteSession()
        }
    }

    fun openMoreSettingsPage(context: KmpContext) {
        authService.getCurrentSession()?.let {
            val moreSettingUrl = "https://${it.serverUrl}/settings/home"
            openExternalUrlUseCase(moreSettingUrl, context)
        }
    }

    fun openRepostSettings(context: KmpContext) {
        authService.getCurrentSession()?.let {
            val moreSettingUrl = "https://${it.serverUrl}/settings/timeline"
            openExternalUrlUseCase(moreSettingUrl, context)
        }
    }
}