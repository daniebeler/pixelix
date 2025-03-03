package com.daniebeler.pfpixelix.ui.composables.session

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniebeler.pfpixelix.domain.service.platform.Platform
import com.daniebeler.pfpixelix.domain.service.session.AuthService
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject

@Inject
class LoginViewModel(
    private val authService: AuthService,
    private val platform: Platform
) : ViewModel() {

    var serverHost by mutableStateOf("")
        private set

    var isLoading by mutableStateOf(false)
        private set

    var isValidHost by mutableStateOf(false)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    fun updateServerHost(host: String) {
        serverHost = host
        isValidHost = authService.isValidHost(serverHost)
    }

    fun auth() {
        viewModelScope.launch {
            try {
                isLoading = true
                error = null
                authService.auth(serverHost)
            } catch (e: Exception) {
                error = e.message
            } finally {
                isLoading = false
            }
        }
    }

    fun showAvailableServers() {
        platform.openUrl("https://pixelfed.org/servers")
    }
}