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
import com.daniebeler.pfpixelix.domain.usecase.GetHideSensitiveContentUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetOwnInstanceDomainUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetUseInAppBrowserUseCase
import com.daniebeler.pfpixelix.domain.usecase.LogoutUseCase
import com.daniebeler.pfpixelix.domain.usecase.OpenExternalUrlUseCase
import com.daniebeler.pfpixelix.domain.usecase.StoreHideSensitiveContentUseCase
import com.daniebeler.pfpixelix.domain.usecase.StoreUseInAppBrowserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreferencesViewModel @Inject constructor(
    private val storeHideSensitiveContentUseCase: StoreHideSensitiveContentUseCase,
    private val getHideSensitiveContentUseCase: GetHideSensitiveContentUseCase,
    private val getUseInAppBrowserUseCase: GetUseInAppBrowserUseCase,
    private val storeUseInAppBrowserUseCase: StoreUseInAppBrowserUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val getOwnInstanceDomainUseCase: GetOwnInstanceDomainUseCase,
    private val openExternalUrlUseCase: OpenExternalUrlUseCase,
    private val getActiveAppIconUseCase: GetActiveAppIconUseCase
) : ViewModel() {

    var isSensitiveContentHidden by mutableStateOf(true)
    var isUsingInAppBrowser by mutableStateOf(true)
    var appIcon by mutableStateOf<ImageBitmap?>(null)

    var cacheSize by mutableStateOf("")

    var versionName by mutableStateOf("")

    init {
        viewModelScope.launch {
            getHideSensitiveContentUseCase().collect { res ->
                isSensitiveContentHidden = res
            }
        }

        viewModelScope.launch {
            getUseInAppBrowserUseCase().collect { res ->
                isUsingInAppBrowser = res
            }
        }


    }

    fun getAppIcon(context: Context){
        appIcon = getActiveAppIconUseCase(context)
    }

    fun getVersionName(context: Context) {
        try {
            versionName = context.packageManager.getPackageInfo(context.packageName, 0).versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
    }

    fun logout() {
        viewModelScope.launch {
            logoutUseCase()
        }
    }

    fun storeHideSensitiveContent(value: Boolean) {
        isSensitiveContentHidden = value
        viewModelScope.launch {
            storeHideSensitiveContentUseCase(value)
        }
    }

    fun storeUseInAppBrowser(value: Boolean) {
        isUsingInAppBrowser = value
        viewModelScope.launch {
            storeUseInAppBrowserUseCase(value)
        }
    }

    fun openMoreSettingsPage(context: Context) {
        viewModelScope.launch {
            val domain = getOwnInstanceDomainUseCase().first()
            val moreSettingUrl = "https://$domain/settings/home"
            openExternalUrlUseCase(context, moreSettingUrl)
        }
    }
}