package com.daniebeler.pfpixelix.domain.usecase

import com.daniebeler.pfpixelix.domain.repository.StorageRepository

class StoreHideAltTextButtonUseCase(
    private val storageRepository: StorageRepository
) {

    suspend operator fun invoke(hideAltTextButton: Boolean) {
        return storageRepository.storeHideAltTextButton(hideAltTextButton)
    }
}