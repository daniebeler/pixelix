package com.daniebeler.pfpixelix.domain.usecase

import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.model.Account
import com.daniebeler.pfpixelix.domain.repository.CountryRepository
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class VerifyTokenUseCase(
    private val repository: CountryRepository
) {
    operator fun invoke(token: String): Flow<Resource<Account>> {
        return repository.verifyToken(token)
    }
}