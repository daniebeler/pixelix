package com.daniebeler.pixelix.domain.usecase

import com.daniebeler.pixelix.domain.repository.StorageRepository

class StoreAccountIdUseCase(
    private val storageRepository: StorageRepository
) {
    suspend operator fun invoke(accountId: String) {
        return storageRepository.storeAccountId(accountId)
    }
}