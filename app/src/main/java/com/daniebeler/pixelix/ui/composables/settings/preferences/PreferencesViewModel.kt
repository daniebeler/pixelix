package com.daniebeler.pixelix.ui.composables.settings.preferences

import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniebeler.pixelix.domain.usecase.GetHideSensitiveContentUseCase
import com.daniebeler.pixelix.domain.usecase.GetOwnInstanceDomainUseCase
import com.daniebeler.pixelix.domain.usecase.LogoutUseCase
import com.daniebeler.pixelix.domain.usecase.StoreHideSensitiveContentUseCase
import com.daniebeler.pixelix.utils.Navigate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreferencesViewModel @Inject constructor(
    private val storeHideSensitiveContentUseCase: StoreHideSensitiveContentUseCase,
    private val getHideSensitiveContentUseCase: GetHideSensitiveContentUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val getOwnInstanceDomainUseCase: GetOwnInstanceDomainUseCase
) : ViewModel() {
    var isSensitiveContentHidden by mutableStateOf(true)

    var cacheSize by mutableStateOf("")

    var versionName by mutableStateOf("")

    init {
        viewModelScope.launch {
            getHideSensitiveContentUseCase().collect { res ->
                isSensitiveContentHidden = res
            }
        }
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

    fun openMoreSettingsPage(context: Context) {
        viewModelScope.launch {
            val domain = getOwnInstanceDomainUseCase().first()
            val moreSettingUrl = "https://$domain/settings/home"
            Navigate().openUrlInApp(context, moreSettingUrl)
        }
    }
}