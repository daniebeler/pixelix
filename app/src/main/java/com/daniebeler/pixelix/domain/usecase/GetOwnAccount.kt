package com.daniebeler.pixelix.domain.usecase

import com.daniebeler.pixelix.common.Resource
import com.daniebeler.pixelix.domain.model.Account
import com.daniebeler.pixelix.domain.repository.CountryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class GetOwnAccount(
    private val repository: CountryRepository
) {
    operator fun invoke(): Flow<Resource<Account>> {
        var accountId: String
        runBlocking {
            accountId = repository.getAccountId().first()
        }

        return repository.getAccount(accountId)
    }
}