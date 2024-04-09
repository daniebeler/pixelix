package com.daniebeler.pfpixelix.domain.usecase

import com.daniebeler.pfpixelix.domain.repository.StorageRepository

class StoreThemeUseCase(
    private val storageRepository: StorageRepository
) {
    suspend operator fun invoke(theme: String) {
        return storageRepository.storeTheme(theme)
    }
}