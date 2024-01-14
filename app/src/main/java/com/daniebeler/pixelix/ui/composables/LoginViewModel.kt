package com.daniebeler.pixelix.ui.composables

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.daniebeler.pixelix.domain.model.Application
import com.daniebeler.pixelix.domain.repository.CountryRepository
import com.daniebeler.pixelix.utils.Navigate
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: CountryRepository
) : ViewModel() {

    private var _authApplication: Application? = null
    var customUrl: String by mutableStateOf("")
    var loading: Boolean by mutableStateOf(false)
    private suspend fun registerApplication(): String {
        _authApplication = repository.createApplication()
        if (_authApplication != null) {
            repository.storeClientId(_authApplication!!.clientId)
            repository.storeClientSecret(_authApplication!!.clientSecret)
            return _authApplication!!.clientId
        }
        return ""
    }

    private suspend fun setBaseUrl(_baseUrl: String): String {
        var baseUrl = _baseUrl
        if (!baseUrl.startsWith("https://")) {
            baseUrl = "https://$baseUrl"
        }
        repository.storeBaseUrl(baseUrl)
        return baseUrl
    }

    suspend fun login(baseUrl: String, context: Context) {
        loading = true
        if (baseUrl != "") {
            val newBaseUrl = setBaseUrl(baseUrl)
            val clientId = registerApplication()

            if (clientId.isNotEmpty()) {
                openUrl(context, clientId, newBaseUrl)
            }
        }
        loading = false
    }

    private fun openUrl(context: Context, clientId: String, baseUrl: String) {
        val url =
            "${baseUrl}/oauth/authorize?response_type=code&redirect_uri=pixelix-android-auth://callback&client_id=" + clientId
        Navigate().openUrlInApp(context, url)
    }
}