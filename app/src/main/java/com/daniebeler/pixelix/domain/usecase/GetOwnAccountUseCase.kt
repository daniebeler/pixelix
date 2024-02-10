package com.daniebeler.pixelix.domain.usecase

import com.daniebeler.pixelix.common.Resource
import com.daniebeler.pixelix.domain.model.Account
import com.daniebeler.pixelix.domain.repository.CountryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class GetOwnAccountUseCase(
    private val repository: CountryRepository
) {
    operator fun invoke(): Flow<Resource<Account>> = flow {
        emit(Resource.Loading())
        val accountId = repository.getAccountId().first()
        if (accountId.isNotBlank()) {
            repository.getAccount(accountId).collect { res ->
                emit(res)
            }
        }
    }
}