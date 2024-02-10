package com.daniebeler.pixelix.domain.usecase

import com.daniebeler.pixelix.common.Resource
import com.daniebeler.pixelix.domain.model.Account
import com.daniebeler.pixelix.domain.repository.CountryRepository
import kotlinx.coroutines.flow.Flow

class GetAccountsFollowersUseCase(
    private val repository: CountryRepository
) {
    operator fun invoke(accountId: String, maxId: String = ""): Flow<Resource<List<Account>>> {
        return repository.getAccountsFollowers(accountId, maxId)
    }
}