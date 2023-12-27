package com.daniebeler.pixels

import androidx.lifecycle.AndroidViewModel
import com.daniebeler.pixels.domain.repository.CountryRepository
import com.daniebeler.pixels.domain.model.Application
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: CountryRepository,
    application: android.app.Application
): AndroidViewModel(application) {



    suspend fun obtainToken(code: String): Boolean {
        val clientId: String = getClientIdFromStorage().first()
        val clientSecret: String = getClientSecretFromStorage().first()

        val token = repository.obtainToken(clientId, clientSecret, code)

        if (token != null) {
            storeAccessToken(token.accessToken)
            return true
        }

        return false
    }

    fun doesTokenExist(): Boolean {
        return repository.doesAccessTokenExist()
    }


    suspend fun storeClientId(clientId: String) {
        repository.storeClientId(clientId)
    }

    fun getClientIdFromStorage(): Flow<String> {
        return repository.getClientIdFromStorage()
    }

    suspend fun storeClientSecret(clientSecret: String) {
        repository.storeClientSecret(clientSecret)
    }

    fun getClientSecretFromStorage(): Flow<String> {
        return repository.getClientSecretFromStorage()
    }

    suspend fun storeAccessToken(accessToken: String) {
        repository.storeAccessToken(accessToken)
    }

    suspend fun logout() {
        storeAccessToken("")
        storeClientId("")
        storeClientSecret("")

    }

}