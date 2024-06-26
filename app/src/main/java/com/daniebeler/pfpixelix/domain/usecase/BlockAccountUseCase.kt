package com.daniebeler.pfpixelix.domain.usecase

import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.model.Relationship
import com.daniebeler.pfpixelix.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow

class BlockAccountUseCase(
    private val accountRepository: AccountRepository
) {
    operator fun invoke(accountId: String = ""): Flow<Resource<Relationship>> {
        return accountRepository.blockAccount(accountId)
    }
}