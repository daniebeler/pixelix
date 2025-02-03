package com.daniebeler.pfpixelix.domain.usecase

import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.model.Search
import com.daniebeler.pfpixelix.domain.repository.CountryRepository
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class SearchUseCase(
    private val repository: CountryRepository
) {
    operator fun invoke(searchText: String, type: String? = null, limit: Int = 5): Flow<Resource<Search>> {
        return repository.search(searchText, type, limit)
    }
}