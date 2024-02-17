package com.daniebeler.pixelix.domain.usecase

import com.daniebeler.pixelix.common.Resource
import com.daniebeler.pixelix.domain.model.LikedPostsWithNext
import com.daniebeler.pixelix.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow

class GetLikedPostsUseCase(
    private val postRepository: PostRepository
) {
    operator fun invoke(nextId: String = ""): Flow<Resource<LikedPostsWithNext>> {
        return postRepository.getLikedPosts(nextId)
    }
}