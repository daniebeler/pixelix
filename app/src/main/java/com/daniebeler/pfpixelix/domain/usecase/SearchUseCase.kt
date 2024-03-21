package com.daniebeler.pfpixelix.domain.usecase

import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.model.Search
import com.daniebeler.pfpixelix.domain.repository.CountryRepository
import kotlinx.coroutines.flow.Flow

class SearchUseCase(
    private val repository: CountryRepository
) {
    operator fun invoke(searchText: String): Flow<Resource<Search>> {
        return repository.search(searchText, null)
    }

    operator fun invoke(searchText: String, type: String?): Flow<Resource<Search>> {
        return repository.search(searchText, type)
    }
}