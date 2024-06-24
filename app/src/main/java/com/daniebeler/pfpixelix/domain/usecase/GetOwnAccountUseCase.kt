package com.daniebeler.pfpixelix.domain.usecase

import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.model.Account
import com.daniebeler.pfpixelix.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetOwnAccountUseCase(
    private val currentLoginDataUseCase: GetCurrentLoginDataUseCase,
    private val accountRepository: AccountRepository
) {
    operator fun invoke(): Flow<Resource<Account>> = flow {
        emit(Resource.Loading())
        val accountId = currentLoginDataUseCase()!!.accountId
        if (accountId.isNotBlank()) {
            accountRepository.getAccount(accountId).collect { res ->
                emit(res)
            }
        }
    }
}