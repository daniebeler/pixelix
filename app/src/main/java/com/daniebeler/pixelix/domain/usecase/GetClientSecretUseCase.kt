package com.daniebeler.pixelix.domain.usecase

import com.daniebeler.pixelix.domain.repository.CountryRepository
import kotlinx.coroutines.flow.Flow

class GetClientSecretUseCase(
    private val repository: CountryRepository
) {
    operator fun invoke(): Flow<String> {
        return repository.getClientSecretFromStorage()
    }
}