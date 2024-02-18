package com.daniebeler.pfpixelix.domain.usecase

import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.model.LikedPostsWithNext
import com.daniebeler.pfpixelix.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow

class GetLikedPostsUseCase(
    private val postRepository: PostRepository
) {
    operator fun invoke(nextId: String = ""): Flow<Resource<LikedPostsWithNext>> {
        return postRepository.getLikedPosts(nextId)
    }
}