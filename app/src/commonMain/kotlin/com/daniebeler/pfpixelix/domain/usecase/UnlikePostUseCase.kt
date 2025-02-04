package com.daniebeler.pfpixelix.domain.usecase

import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.model.Post
import com.daniebeler.pfpixelix.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class UnlikePostUseCase(
    private val postRepository: PostRepository
) {
    operator fun invoke(postId: String): Flow<Resource<Post>> {
        return postRepository.unlikePost(postId)
    }
}