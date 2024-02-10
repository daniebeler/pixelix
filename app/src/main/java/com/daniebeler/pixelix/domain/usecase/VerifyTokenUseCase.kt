package com.daniebeler.pixelix.domain.usecase

import com.daniebeler.pixelix.common.Resource
import com.daniebeler.pixelix.domain.model.Account
import com.daniebeler.pixelix.domain.repository.CountryRepository
import kotlinx.coroutines.flow.Flow

class VerifyTokenUseCase(
    private val repository: CountryRepository
) {
    operator fun invoke(token: String): Flow<Resource<Account>> {
        return repository.verifyToken(token)
    }
}