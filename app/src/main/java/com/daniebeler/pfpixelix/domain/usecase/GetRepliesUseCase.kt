package com.daniebeler.pfpixelix.domain.usecase

import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.model.Reply
import com.daniebeler.pfpixelix.domain.repository.CountryRepository
import kotlinx.coroutines.flow.Flow

class GetRepliesUseCase(
    private val repository: CountryRepository
) {
    operator fun invoke(userId: String, postId: String): Flow<Resource<List<Reply>>> {
        return repository.getReplies(userId, postId)
    }
}