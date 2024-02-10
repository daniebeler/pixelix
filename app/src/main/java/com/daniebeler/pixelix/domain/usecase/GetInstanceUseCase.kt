package com.daniebeler.pixelix.domain.usecase

import com.daniebeler.pixelix.common.Resource
import com.daniebeler.pixelix.domain.model.Instance
import com.daniebeler.pixelix.domain.repository.CountryRepository
import kotlinx.coroutines.flow.Flow

class GetInstanceUseCase(
    private val repository: CountryRepository
) {
    operator fun invoke(): Flow<Resource<Instance>> {
        return repository.getInstance()
    }
}