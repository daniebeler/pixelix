package com.daniebeler.pfpixelix.domain.usecase

import com.daniebeler.pfpixelix.domain.repository.StorageRepository

class StoreAccountIdUseCase(
    private val storageRepository: StorageRepository
) {
    suspend operator fun invoke(accountId: String) {
        return storageRepository.storeAccountId(accountId)
    }
}