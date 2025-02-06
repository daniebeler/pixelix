package com.daniebeler.pfpixelix.ui.composables

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.daniebeler.pfpixelix.domain.model.Application
import com.daniebeler.pfpixelix.domain.model.LoginData
import com.daniebeler.pfpixelix.domain.repository.CountryRepository
import com.daniebeler.pfpixelix.domain.usecase.AddNewLoginUseCase
import com.daniebeler.pfpixelix.domain.usecase.UpdateLoginDataUseCase
import com.daniebeler.pfpixelix.utils.KmpContext
import com.daniebeler.pfpixelix.utils.Navigate
import me.tatarka.inject.annotations.Inject

class LoginViewModel @Inject constructor(
    private val repository: CountryRepository,
    private val newLoginDataUseCase: AddNewLoginUseCase,
    private val updateLoginDataUseCase: UpdateLoginDataUseCase
) : ViewModel() {

    private val domainRegex: Regex =
        "^((\\*)|((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)|((\\*\\.)?([a-zA-Z0-9-]+\\.){0,5}[a-zA-Z0-9-][a-zA-Z0-9-]+\\.[a-zA-Z]{2,63}?))\$".toRegex()

    private var _authApplication: Application? = null
    var customUrl: String by mutableStateOf("")
    var isValidUrl: Boolean by mutableStateOf(false)

    var loading: Boolean by mutableStateOf(false)


    private suspend fun registerApplication(): String {
        _authApplication = repository.createApplication()
        if (_authApplication != null) {
            return _authApplication!!.clientId
        }
        return ""
    }

    private suspend fun setBaseUrl(_baseUrl: String): String {
        var baseUrl = _baseUrl
        if (!baseUrl.startsWith("https://")) {
            baseUrl = "https://$baseUrl"
        }
        newLoginDataUseCase(LoginData(baseUrl = baseUrl, loginOngoing = true))
        return baseUrl
    }

    fun domainChanged() {
        isValidUrl = domainRegex.matches(customUrl)
    }

    suspend fun login(baseUrl: String, context: KmpContext) {
        loading = true
        if (domainRegex.matches(baseUrl)) {
            val newBaseUrl = setBaseUrl(baseUrl)
            val authApplication = repository.createApplication()
            if (authApplication != null) {
                updateLoginDataUseCase(
                    LoginData(
                        baseUrl = baseUrl,
                        clientId = authApplication.clientId,
                        clientSecret = authApplication.clientSecret,
                        loginOngoing = true
                    )
                )
                openUrl(context, authApplication.clientId, newBaseUrl)
            }
        }
        loading = false
    }

    private fun openUrl(context: KmpContext, clientId: String, baseUrl: String) {
        val url =
            "${baseUrl}/oauth/authorize?response_type=code&redirect_uri=pixelix-android-auth://callback&client_id=" + clientId
        Navigate.openUrlInApp(context, url)
    }
}