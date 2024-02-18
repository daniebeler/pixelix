package com.daniebeler.pfpixelix.domain.usecase

import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.model.Post
import com.daniebeler.pfpixelix.domain.repository.PostRepository
import com.daniebeler.pfpixelix.domain.repository.StorageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class GetOwnPostsUseCase(
    private val postRepository: PostRepository, private val storageRepository: StorageRepository
) {
    operator fun invoke(maxPostId: String = ""): Flow<Resource<List<Post>>> = flow {
        emit(Resource.Loading())

        val accountId = storageRepository.getAccountId().first()
        if (accountId.isNotEmpty()) {
            postRepository.getPostsByAccountId(accountId, maxPostId).collect { res ->
                emit(res)
            }
        }
    }
}