package com.daniebeler.pixelix

import androidx.lifecycle.AndroidViewModel
import com.daniebeler.pixelix.domain.repository.CountryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: CountryRepository,
    application: android.app.Application
) : AndroidViewModel(application) {


    suspend fun obtainToken(code: String): Boolean {
        val clientId: String = getClientIdFromStorage().first()
        val clientSecret: String = getClientSecretFromStorage().first()

        val token = repository.obtainToken(clientId, clientSecret, code) ?: return false

        storeAccessToken(token.accessToken)
        val account = repository.verifyToken(token.accessToken) ?: return false
        repository.storeAccountId(account.id)
        return true

    }


    fun doesTokenExist(): Boolean {
        return repository.doesAccessTokenExist()
    }

    private fun getClientIdFromStorage(): Flow<String> {
        return repository.getClientIdFromStorage()
    }

    private fun getClientSecretFromStorage(): Flow<String> {
        return repository.getClientSecretFromStorage()
    }

    private suspend fun storeAccessToken(accessToken: String) {
        repository.storeAccessToken(accessToken)
    }

}