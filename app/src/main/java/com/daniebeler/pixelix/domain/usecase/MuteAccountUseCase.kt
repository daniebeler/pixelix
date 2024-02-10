package com.daniebeler.pixelix.domain.usecase

import com.daniebeler.pixelix.common.Resource
import com.daniebeler.pixelix.domain.model.Relationship
import com.daniebeler.pixelix.domain.repository.CountryRepository
import kotlinx.coroutines.flow.Flow

class MuteAccountUseCase(
    private val repository: CountryRepository
) {
    operator fun invoke(accountId: String = ""): Flow<Resource<Relationship>> {
        return repository.muteAccount(accountId)
    }
}