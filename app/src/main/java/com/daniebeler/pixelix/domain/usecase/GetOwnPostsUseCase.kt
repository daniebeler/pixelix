package com.daniebeler.pixelix.domain.usecase

import com.daniebeler.pixelix.common.Resource
import com.daniebeler.pixelix.domain.model.Post
import com.daniebeler.pixelix.domain.repository.PostRepository
import com.daniebeler.pixelix.domain.repository.StorageRepository
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