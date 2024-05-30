package com.daniebeler.pfpixelix.domain.usecase

import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.model.Post
import com.daniebeler.pfpixelix.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetOwnPostsUseCase(
    private val postRepository: PostRepository, private val currentLoginDataUseCase: GetCurrentLoginDataUseCase
) {
    operator fun invoke(maxPostId: String = ""): Flow<Resource<List<Post>>> = flow {
        emit(Resource.Loading())

        val accountId = currentLoginDataUseCase()!!.accountId
        if (accountId.isNotEmpty()) {
            postRepository.getPostsByAccountId(accountId, maxPostId).collect { res ->
                emit(res)
            }
        }
    }
}