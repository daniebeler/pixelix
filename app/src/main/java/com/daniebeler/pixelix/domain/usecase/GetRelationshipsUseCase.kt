package com.daniebeler.pixelix.domain.usecase

import com.daniebeler.pixelix.common.Resource
import com.daniebeler.pixelix.domain.model.Relationship
import com.daniebeler.pixelix.domain.repository.CountryRepository
import kotlinx.coroutines.flow.Flow

class GetRelationshipsUseCase(
    private val repository: CountryRepository
) {
    operator fun invoke(accountIds: List<String>): Flow<Resource<List<Relationship>>> {
        return repository.getRelationships(accountIds)
    }
}