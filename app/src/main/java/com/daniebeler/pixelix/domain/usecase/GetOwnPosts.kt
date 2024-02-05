package com.daniebeler.pixelix.domain.usecase

import com.daniebeler.pixelix.common.Resource
import com.daniebeler.pixelix.domain.model.Post
import com.daniebeler.pixelix.domain.repository.CountryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class GetOwnPosts(
    private val repository: CountryRepository
) {
    operator fun invoke(maxPostId: String = ""): Flow<Resource<List<Post>>> {
        var accountId = ""
        runBlocking {
            accountId = repository.getAccountId().first()
        }

        return repository.getPostsByAccountId(accountId, maxPostId)
    }
}