package com.daniebeler.pfpixelix.domain.usecase.nodeinfo

import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.model.nodeinfo.FediServer
import com.daniebeler.pfpixelix.domain.repository.CountryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetFediServerUseCase(private val countryRepository: CountryRepository) {

    operator fun invoke(domain: String): Flow<Resource<FediServer>> = flow {
        countryRepository.getServerFromFediDB(domain)
            .collect { res ->
                if (res is Resource.Error) {
                    emit(Resource.Error(res.message!!))
                }
                if (res is Resource.Success) {

                    emit(res)
                }
            }
    }
}

