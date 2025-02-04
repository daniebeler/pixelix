package com.daniebeler.pfpixelix.domain.usecase

import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.model.Account
import com.daniebeler.pfpixelix.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class GetAccountsFollowersUseCase(
    private val accountRepository: AccountRepository
) {
    operator fun invoke(accountId: String, maxId: String = ""): Flow<Resource<List<Account>>> {
        return accountRepository.getAccountsFollowers(accountId, maxId)
    }
}