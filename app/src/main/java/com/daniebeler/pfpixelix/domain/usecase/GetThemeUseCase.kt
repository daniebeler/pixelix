package com.daniebeler.pfpixelix.domain.usecase

import com.daniebeler.pfpixelix.domain.repository.StorageRepository
import kotlinx.coroutines.flow.Flow

class GetThemeUseCase(
    private val storageRepository: StorageRepository
) {
    operator fun invoke(): Flow<String> {
        return storageRepository.getStoreTheme()
    }
}