package com.daniebeler.pfpixelix.domain.usecase

import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.model.Account
import com.daniebeler.pfpixelix.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow

class GetBlockedAccountsUseCase(
    private val accountRepository: AccountRepository
) {
    operator fun invoke(): Flow<Resource<List<Account>>> {
        return accountRepository.getBlockedAccounts()
    }
}