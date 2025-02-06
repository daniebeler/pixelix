package com.daniebeler.pfpixelix.ui.composables.settings.preferences

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
import com.daniebeler.pfpixelix.utils.KmpContext
import com.daniebeler.pfpixelix.utils.appVersionName
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject

class PreferencesViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase,
    private val getOwnInstanceDomainUseCase: GetOwnInstanceDomainUseCase,
    private val openExternalUrlUseCase: OpenExternalUrlUseCase,
    private val getActiveAppIconUseCase: GetActiveAppIconUseCase
) : ViewModel() {

    var appIcon by mutableStateOf<ImageBitmap?>(null)
    var versionName by mutableStateOf("")

    fun getAppIcon(context: KmpContext) {
        appIcon = getActiveAppIconUseCase(context)
    }

    fun getVersionName(context: KmpContext) {
        versionName = context.appVersionName
    }

    fun logout() {
        viewModelScope.launch {
            logoutUseCase()
        }
    }

    fun openMoreSettingsPage(context: KmpContext) {
        viewModelScope.launch {
            val domain = getOwnInstanceDomainUseCase()
            val moreSettingUrl = "https://$domain/settings/home"
            openExternalUrlUseCase(moreSettingUrl, context)
        }
    }

    fun openRepostSettings(context: KmpContext) {
        viewModelScope.launch {
            val domain = getOwnInstanceDomainUseCase()
            val moreSettingUrl = "https://$domain/settings/timeline"
            openExternalUrlUseCase(moreSettingUrl, context)
        }
    }
}