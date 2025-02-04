package com.daniebeler.pfpixelix.domain.usecase

import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.model.Relationship
import com.daniebeler.pfpixelix.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class UnmuteAccountUseCase(
    private val accountRepository: AccountRepository
) {
    operator fun invoke(accountId: String = ""): Flow<Resource<Relationship>> {
        return accountRepository.unMuteAccount(accountId)
    }
}