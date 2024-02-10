package com.daniebeler.pixelix.domain.usecase

import com.daniebeler.pixelix.domain.repository.StorageRepository
import kotlinx.coroutines.flow.Flow

class GetOwnAccountIdUseCase(
    private val repository: StorageRepository
) {
    operator fun invoke(): Flow<String> {
        return repository.getAccountId()
    }
}