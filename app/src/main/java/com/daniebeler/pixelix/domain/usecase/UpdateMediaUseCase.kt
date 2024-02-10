package com.daniebeler.pixelix.domain.usecase

import com.daniebeler.pixelix.common.Resource
import com.daniebeler.pixelix.domain.model.MediaAttachment
import com.daniebeler.pixelix.domain.repository.CountryRepository
import kotlinx.coroutines.flow.Flow

class UpdateMediaUseCase(
    private val repository: CountryRepository
) {
    operator fun invoke(id: String, description: String): Flow<Resource<MediaAttachment>> {
        return repository.updateMedia(id, description)
    }
}