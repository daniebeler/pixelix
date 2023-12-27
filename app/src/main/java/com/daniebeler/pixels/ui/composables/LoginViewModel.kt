package com.daniebeler.pixels.ui.composables

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.daniebeler.pixels.domain.model.Application
import com.daniebeler.pixels.domain.repository.CountryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: CountryRepository
) : ViewModel() {

    private var _authApplication: Application? = null
    var customUrl: String by mutableStateOf("")
    private suspend fun registerApplication(): String {
        _authApplication = repository.createApplication()
        if (_authApplication != null) {
            repository.storeClientId(_authApplication!!.clientId)
            repository.storeClientSecret(_authApplication!!.clientSecret)
            println("Client_Id ${_authApplication!!.clientId}")
            return _authApplication!!.clientId
        }
        return ""
    }

    suspend fun setBaseUrl(baseUrl: String): String {
        repository.storeBaseUrl(baseUrl)
        return registerApplication()
    }
}