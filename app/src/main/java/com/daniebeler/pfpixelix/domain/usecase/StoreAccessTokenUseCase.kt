package com.daniebeler.pfpixelix.domain.usecase

import com.daniebeler.pfpixelix.domain.repository.CountryRepository

class StoreAccessTokenUseCase(
    private val repository: CountryRepository
) {
    suspend operator fun invoke(accessToken: String) {
        return repository.storeAccessToken(accessToken)
    }
}