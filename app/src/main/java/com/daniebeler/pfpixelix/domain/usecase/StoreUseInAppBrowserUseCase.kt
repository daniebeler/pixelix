package com.daniebeler.pfpixelix.domain.usecase

import com.daniebeler.pfpixelix.domain.repository.StorageRepository

class StoreUseInAppBrowserUseCase(
    private val storageRepository: StorageRepository
) {

    suspend operator fun invoke(useInAppBrowser: Boolean) {
        return storageRepository.storeUseInAppBrowser(useInAppBrowser)
    }
}