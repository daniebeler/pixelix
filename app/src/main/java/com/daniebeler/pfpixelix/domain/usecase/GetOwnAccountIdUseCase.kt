package com.daniebeler.pfpixelix.domain.usecase

import com.daniebeler.pfpixelix.domain.repository.StorageRepository
import kotlinx.coroutines.flow.Flow

class GetOwnAccountIdUseCase(
    private val repository: StorageRepository
) {
    operator fun invoke(): Flow<String> {
        return repository.getAccountId()
    }
}