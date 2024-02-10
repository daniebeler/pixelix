package com.daniebeler.pixelix.domain.usecase

import com.daniebeler.pixelix.domain.repository.StorageRepository

class StoreHideSensitiveContentUseCase(
    private val storageRepository: StorageRepository
) {

    suspend operator fun invoke(hideSensitiveContent: Boolean) {
        return storageRepository.storeHideSensitiveContent(hideSensitiveContent)
    }
}