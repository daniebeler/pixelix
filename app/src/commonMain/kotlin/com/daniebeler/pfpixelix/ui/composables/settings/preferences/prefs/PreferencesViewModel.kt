package com.daniebeler.pfpixelix.ui.composables.settings.preferences

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniebeler.pfpixelix.domain.service.icon.AppIconService
import com.daniebeler.pfpixelix.domain.service.platform.Platform
import com.daniebeler.pfpixelix.domain.service.session.AuthService
import com.daniebeler.pfpixelix.utils.KmpContext
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject
import org.jetbrains.compose.resources.DrawableResource
import pixelix.app.generated.resources.Res
import pixelix.app.generated.resources.pixelix_logo

class PreferencesViewModel @Inject constructor(
    private val authService: AuthService,
    private val platform: Platform,
    private val appIconService: AppIconService
) : ViewModel() {
    var appIcon by mutableStateOf<DrawableResource>(Res.drawable.pixelix_logo)
    var versionName by mutableStateOf("")

    fun getAppIcon() {
        appIcon = appIconService.getCurrentIcon()
    }

    fun getVersionName() {
        versionName = platform.getAppVersion()
    }

    fun logout() {
        viewModelScope.launch {
            authService.deleteSession()
        }
    }

    fun openMoreSettingsPage(context: KmpContext) {
        authService.getCurrentSession()?.let {
            platform.openUrl("https://${it.serverUrl}/settings/home")
        }
    }

    fun openRepostSettings(context: KmpContext) {
        authService.getCurrentSession()?.let {
            platform.openUrl("https://${it.serverUrl}/settings/timeline")
        }
    }
}