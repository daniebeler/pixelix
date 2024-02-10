package com.daniebeler.pixelix.domain.usecase

import com.daniebeler.pixelix.common.Resource
import com.daniebeler.pixelix.domain.model.Post
import com.daniebeler.pixelix.domain.repository.CountryRepository
import kotlinx.coroutines.flow.Flow

class CreateReplyUseCase(
    private val repository: CountryRepository
) {
    operator fun invoke(postId: String, content: String): Flow<Resource<Post>> {
        return repository.createReply(postId, content)
    }
}