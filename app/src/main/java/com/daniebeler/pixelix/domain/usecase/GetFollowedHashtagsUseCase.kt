package com.daniebeler.pixelix.domain.usecase

import com.daniebeler.pixelix.common.Resource
import com.daniebeler.pixelix.domain.model.Tag
import com.daniebeler.pixelix.domain.repository.CountryRepository
import kotlinx.coroutines.flow.Flow

class GetFollowedHashtagsUseCase(
    private val repository: CountryRepository
) {
    operator fun invoke(): Flow<Resource<List<Tag>>> {
        return repository.getFollowedHashtags()
    }
}