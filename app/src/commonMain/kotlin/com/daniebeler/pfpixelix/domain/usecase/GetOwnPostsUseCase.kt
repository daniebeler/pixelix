package com.daniebeler.pfpixelix.domain.usecase

import com.daniebeler.pfpixelix.common.Constants
import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.model.Post
import com.daniebeler.pfpixelix.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import me.tatarka.inject.annotations.Inject

@Inject
class GetOwnPostsUseCase(
    private val postRepository: PostRepository, private val currentLoginDataUseCase: GetCurrentLoginDataUseCase
) {
    operator fun invoke(
        maxPostId: String = "", limit: Int = Constants.PROFILE_POSTS_LIMIT
    ): Flow<Resource<List<Post>>> = flow {
        emit(Resource.Loading())

        val accountId = currentLoginDataUseCase()?.accountId
        accountId?.let {
            postRepository.getPostsByAccountId(accountId, maxPostId, limit).collect { res ->
                emit(res)
            }
        }
    }
}