package com.daniebeler.pixelix.domain.usecase

import com.daniebeler.pixelix.domain.repository.CountryRepository

class StoreAccessTokenUseCase(
    private val repository: CountryRepository
) {
    suspend operator fun invoke(accessToken: String) {
        return repository.storeAccessToken(accessToken)
    }
}