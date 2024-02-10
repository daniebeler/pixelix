package com.daniebeler.pixelix.domain.usecase

import com.daniebeler.pixelix.common.Resource
import com.daniebeler.pixelix.domain.model.AccessToken
import com.daniebeler.pixelix.domain.model.Post
import com.daniebeler.pixelix.domain.repository.CountryRepository
import kotlinx.coroutines.flow.Flow

class ObtainTokenUseCase(
    private val repository: CountryRepository
) {
    operator fun invoke(clientId: String, clientSecret: String, code: String): Flow<Resource<AccessToken>> {
        return repository.obtainToken(clientId, clientSecret, code)
    }
}