package com.daniebeler.pixelix.domain.usecase

import com.daniebeler.pixelix.domain.repository.CountryRepository

class StoreAccountIdUseCase(
    private val repository: CountryRepository
) {
    suspend operator fun invoke(accountId: String) {
        return repository.storeAccountId(accountId)
    }
}