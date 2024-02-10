package com.daniebeler.pixelix.domain.usecase

import com.daniebeler.pixelix.common.Resource
import com.daniebeler.pixelix.domain.model.LikedPostsWithNext
import com.daniebeler.pixelix.domain.repository.CountryRepository
import kotlinx.coroutines.flow.Flow

class GetLikedPostsUseCase(
    private val repository: CountryRepository
) {
    operator fun invoke(nextId: String = ""): Flow<Resource<LikedPostsWithNext>> {
        return repository.getLikedPosts(nextId)
    }
}