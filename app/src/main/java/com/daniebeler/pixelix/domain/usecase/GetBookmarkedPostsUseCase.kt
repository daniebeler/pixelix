package com.daniebeler.pixelix.domain.usecase

import com.daniebeler.pixelix.common.Resource
import com.daniebeler.pixelix.domain.model.Post
import com.daniebeler.pixelix.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow

class GetBookmarkedPostsUseCase(
    private val postRepository: PostRepository
) {
    operator fun invoke(): Flow<Resource<List<Post>>> {
        return postRepository.getBookmarkedPosts()
    }
}