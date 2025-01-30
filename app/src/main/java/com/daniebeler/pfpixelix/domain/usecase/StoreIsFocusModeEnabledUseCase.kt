package com.daniebeler.pfpixelix.domain.usecase

import com.daniebeler.pfpixelix.domain.repository.StorageRepository

class StoreIsFocusModeEnabledUseCase(
    private val storageRepository: StorageRepository
) {

    suspend operator fun invoke(focusModeEnabled: Boolean) {
        return storageRepository.storeIsFocusModeEnabled(focusModeEnabled)
    }
}