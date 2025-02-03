package com.daniebeler.pfpixelix.domain.usecase

import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.model.Relationship
import com.daniebeler.pfpixelix.domain.repository.CountryRepository
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class GetRelationshipsUseCase(
    private val repository: CountryRepository
) {
    operator fun invoke(accountIds: List<String>): Flow<Resource<List<Relationship>>> {
        return repository.getRelationships(accountIds)
    }
}