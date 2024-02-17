package com.daniebeler.pixelix.domain.usecase

import com.daniebeler.pixelix.common.Resource
import com.daniebeler.pixelix.domain.model.Relationship
import com.daniebeler.pixelix.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow

class UnmuteAccountUseCase(
    private val accountRepository: AccountRepository
) {
    operator fun invoke(accountId: String = ""): Flow<Resource<Relationship>> {
        return accountRepository.unMuteAccount(accountId)
    }
}