package com.daniebeler.pixelix.domain.usecase

import com.daniebeler.pixelix.common.Resource
import com.daniebeler.pixelix.domain.model.Reply
import com.daniebeler.pixelix.domain.repository.CountryRepository
import kotlinx.coroutines.flow.Flow

class GetRepliesUseCase(
    private val repository: CountryRepository
) {
    operator fun invoke(userId: String, postId: String): Flow<Resource<List<Reply>>> {
        return repository.getReplies(userId, postId)
    }
}