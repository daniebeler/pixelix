package com.daniebeler.pixelix.domain.usecase

import com.daniebeler.pixelix.common.Resource
import com.daniebeler.pixelix.domain.model.Tag
import com.daniebeler.pixelix.domain.repository.CountryRepository
import kotlinx.coroutines.flow.Flow

class FollowHashtagUseCase(
    private val repository: CountryRepository
) {
    operator fun invoke(hashtag: String): Flow<Resource<Tag>> {
        return repository.followHashtag(hashtag)
    }
}