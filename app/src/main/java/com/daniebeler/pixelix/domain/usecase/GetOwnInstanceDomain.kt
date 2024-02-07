package com.daniebeler.pixelix.domain.usecase

import com.daniebeler.pixelix.domain.repository.StorageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetOwnInstanceDomain(
    private val repository: StorageRepository
) {
    operator fun invoke(): Flow<String> = flow {
        repository.getBaseUrlFromStorage().collect { fief ->
            val res = fief.substringAfter("https://")
            emit(res)
        }
    }
}