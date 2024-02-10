package com.daniebeler.pixelix.domain.usecase

import com.daniebeler.pixelix.common.Resource
import com.daniebeler.pixelix.domain.model.Account
import com.daniebeler.pixelix.domain.repository.CountryRepository
import kotlinx.coroutines.flow.Flow

class GetAccountsWhoLikedPostUseCase(
    private val repository: CountryRepository
) {
    operator fun invoke(postId: String): Flow<Resource<List<Account>>> {
        return repository.getLikedBy(postId)
    }
}