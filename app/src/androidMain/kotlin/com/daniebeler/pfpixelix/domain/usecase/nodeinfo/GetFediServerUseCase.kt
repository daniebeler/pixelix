package com.daniebeler.pfpixelix.domain.usecase.nodeinfo

import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.model.nodeinfo.FediServer
import com.daniebeler.pfpixelix.domain.repository.CountryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import me.tatarka.inject.annotations.Inject

@Inject
class GetFediServerUseCase(private val countryRepository: CountryRepository) {

    operator fun invoke(domain: String): Flow<Resource<FediServer>> {
        return countryRepository.getServerFromFediDB(domain)
    }
}

