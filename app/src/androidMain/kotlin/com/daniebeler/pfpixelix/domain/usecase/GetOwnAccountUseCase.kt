package com.daniebeler.pfpixelix.domain.usecase

import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.model.Account
import com.daniebeler.pfpixelix.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import me.tatarka.inject.annotations.Inject

@Inject
class GetOwnAccountUseCase(
    private val currentLoginDataUseCase: GetCurrentLoginDataUseCase,
    private val accountRepository: AccountRepository
) {
    operator fun invoke(): Flow<Resource<Account>> = flow {
        emit(Resource.Loading())
        val account = currentLoginDataUseCase()
        if (account != null) {
            accountRepository.getAccount(account.accountId).collect { res ->
                emit(res)
            }
        } else {
            emit(Resource.Error("No account found"))
        }
    }
}