package com.daniebeler.pfpixelix.domain.usecase.nodeinfo

import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.model.nodeinfo.FediSoftware
import com.daniebeler.pfpixelix.domain.repository.CountryRepository
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
actual class GetFediSoftwareUseCase actual constructor(countryRepository: CountryRepository) {
    actual operator fun invoke(domain: String): Flow<Resource<FediSoftware>> {
        TODO("Not yet implemented")
    }
}