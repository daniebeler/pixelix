package com.daniebeler.pfpixelix.domain.usecase

import com.daniebeler.pfpixelix.domain.repository.StorageRepository

class StoreHideSensitiveContentUseCase(
    private val storageRepository: StorageRepository
) {

    suspend operator fun invoke(hideSensitiveContent: Boolean) {
        return storageRepository.storeHideSensitiveContent(hideSensitiveContent)
    }
}