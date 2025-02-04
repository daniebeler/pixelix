package com.daniebeler.pfpixelix.domain.usecase

import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.model.AccessToken
import com.daniebeler.pfpixelix.domain.repository.CountryRepository
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class ObtainTokenUseCase(
    private val repository: CountryRepository
) {
    operator fun invoke(clientId: String, clientSecret: String, code: String): Flow<Resource<AccessToken>> {
        return repository.obtainToken(clientId, clientSecret, code)
    }
}