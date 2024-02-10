package com.daniebeler.pixelix.domain.usecase

import com.daniebeler.pixelix.domain.repository.CountryRepository

class LogoutUseCase(
    private val repository: CountryRepository
) {
    suspend operator fun invoke(accountId: String = "") {
        storeAccessToken("")
        storeClientId("")
        storeClientSecret("")
    }

    private suspend fun storeClientId(clientId: String) {
        repository.storeClientId(clientId)
    }

    private suspend fun storeClientSecret(clientSecret: String) {
        repository.storeClientSecret(clientSecret)
    }

    private suspend fun storeAccessToken(accessToken: String) {
        repository.storeAccessToken(accessToken)
    }
}