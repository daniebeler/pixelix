package com.daniebeler.pixelix.domain.usecase

import com.daniebeler.pixelix.domain.repository.CountryRepository
import com.daniebeler.pixelix.domain.repository.StorageRepository
import kotlinx.coroutines.flow.Flow

class GetClientSecretUseCase(
    private val storageRepository: StorageRepository
) {
    operator fun invoke(): Flow<String> {
        return storageRepository.getClientSecretFromStorage()
    }
}