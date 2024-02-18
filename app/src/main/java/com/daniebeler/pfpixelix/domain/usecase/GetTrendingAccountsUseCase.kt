package com.daniebeler.pfpixelix.domain.usecase

import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.model.Account
import com.daniebeler.pfpixelix.domain.repository.CountryRepository
import kotlinx.coroutines.flow.Flow

class GetTrendingAccountsUseCase(
    private val repository: CountryRepository
) {
    operator fun invoke(): Flow<Resource<List<Account>>> {
        return repository.getTrendingAccounts()
    }
}