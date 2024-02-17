package com.daniebeler.pixelix.domain.usecase

import com.daniebeler.pixelix.common.Resource
import com.daniebeler.pixelix.domain.model.Account
import com.daniebeler.pixelix.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow

class GetMutualFollowersUseCase(
    private val accountRepository: AccountRepository
) {
    operator fun invoke(accountId: String): Flow<Resource<List<Account>>> {
        return accountRepository.getMutualFollowers(accountId)
    }
}