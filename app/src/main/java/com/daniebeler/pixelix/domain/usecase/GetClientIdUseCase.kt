package com.daniebeler.pixelix.domain.usecase

import com.daniebeler.pixelix.domain.repository.CountryRepository
import kotlinx.coroutines.flow.Flow

class GetClientIdUseCase(
    private val repository: CountryRepository
) {
    operator fun invoke(): Flow<String> {
        return repository.getClientIdFromStorage()
    }
}