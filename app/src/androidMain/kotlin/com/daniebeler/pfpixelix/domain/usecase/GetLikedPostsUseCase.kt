package com.daniebeler.pfpixelix.domain.usecase

import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.model.LikedPostsWithNext
import com.daniebeler.pfpixelix.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import me.tatarka.inject.annotations.Inject

@Inject
class GetLikedPostsUseCase(
    private val postRepository: PostRepository
) {
    operator fun invoke(nextId: String = ""): Flow<Resource<LikedPostsWithNext>> = flow {
        emit(Resource.Loading())
        postRepository.getLikedPosts(nextId).collect { likedPosts ->
            if (likedPosts is Resource.Success && likedPosts.data != null) {
                val res: LikedPostsWithNext =
                    likedPosts.data.copy(posts = likedPosts.data.posts.filter { s -> s.mediaAttachments.isNotEmpty() })
                emit(Resource.Success(res))
            } else {
                emit(likedPosts)
            }
        }
    }
}