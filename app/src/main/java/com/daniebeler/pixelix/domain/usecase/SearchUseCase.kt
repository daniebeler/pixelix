package com.daniebeler.pixelix.domain.usecase

import com.daniebeler.pixelix.common.Resource
import com.daniebeler.pixelix.domain.model.Search
import com.daniebeler.pixelix.domain.repository.CountryRepository
import kotlinx.coroutines.flow.Flow

class SearchUseCase(
    private val repository: CountryRepository
) {
    operator fun invoke(searchText: String): Flow<Resource<Search>> {
        return repository.search(searchText)
    }
}