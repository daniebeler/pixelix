package com.daniebeler.pfpixelix.domain.usecase

import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.model.Place
import com.daniebeler.pfpixelix.domain.repository.CountryRepository
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class SearchLocationUseCase(
    private val repository: CountryRepository
) {
    operator fun invoke(searchText: String): Flow<Resource<List<Place>>> {
        return repository.searchLocations(searchText)
    }
}