package com.daniebeler.pfpixelix.domain.usecase

import com.daniebeler.pfpixelix.domain.repository.CountryRepository
import com.daniebeler.pfpixelix.domain.repository.StorageRepository

class LogoutUseCase(
    private val repository: CountryRepository,
    private val storageRepository: StorageRepository
) {
    suspend operator fun invoke(accountId: String = "") {
        storeAccessToken("")
        storeClientId("")
        storeClientSecret("")
    }

    private suspend fun storeClientId(clientId: String) {
        storageRepository.storeClientId(clientId)
    }

    private suspend fun storeClientSecret(clientSecret: String) {
        storageRepository.storeClientSecret(clientSecret)
    }

    private suspend fun storeAccessToken(accessToken: String) {
        repository.storeAccessToken(accessToken)
    }
}