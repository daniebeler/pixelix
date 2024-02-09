package com.daniebeler.pixelix.domain.usecase

import com.daniebeler.pixelix.common.Resource
import com.daniebeler.pixelix.domain.model.Post
import com.daniebeler.pixelix.domain.repository.CountryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class GetOwnPosts(
    private val repository: CountryRepository
) {
    operator fun invoke(maxPostId: String = ""): Flow<Resource<List<Post>>> = flow {
        emit(Resource.Loading())

        val accountId = repository.getAccountId().first()
        if (accountId.isNotEmpty()) {
            repository.getPostsByAccountId(accountId, maxPostId).collect { res ->
                emit(res)
            }
        }
    }
}