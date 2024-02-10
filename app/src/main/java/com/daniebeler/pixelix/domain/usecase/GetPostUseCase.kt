package com.daniebeler.pixelix.domain.usecase

import com.daniebeler.pixelix.common.Resource
import com.daniebeler.pixelix.domain.model.Post
import com.daniebeler.pixelix.domain.repository.CountryRepository
import kotlinx.coroutines.flow.Flow

class GetPostUseCase(
    private val repository: CountryRepository
) {
    operator fun invoke(postId: String): Flow<Resource<Post>> {
        return repository.getPostById(postId)
    }
}