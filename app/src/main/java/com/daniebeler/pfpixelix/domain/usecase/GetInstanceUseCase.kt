package com.daniebeler.pfpixelix.domain.usecase

import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.model.Instance
import com.daniebeler.pfpixelix.domain.repository.CountryRepository
import kotlinx.coroutines.flow.Flow

class GetInstanceUseCase(
    private val repository: CountryRepository
) {
    operator fun invoke(): Flow<Resource<Instance>> {
        return repository.getInstance()
    }
}