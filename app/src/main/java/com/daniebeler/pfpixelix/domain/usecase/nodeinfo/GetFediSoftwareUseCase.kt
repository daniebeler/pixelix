package com.daniebeler.pfpixelix.domain.usecase.nodeinfo

import com.daniebeler.pfpixelix.R
import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.model.nodeinfo.FediSoftware
import com.daniebeler.pfpixelix.domain.repository.CountryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetFediSoftwareUseCase(private val countryRepository: CountryRepository) {
    operator fun invoke(domain: String): Flow<Resource<FediSoftware>> = flow {
            emit(Resource.Loading())
            countryRepository.getSoftwareFromFediDB(domain)
                .collect { res ->
                    if (res is Resource.Error) {
                        emit(Resource.Error(res.message!!))
                    }
                    if (res is Resource.Success) {
                        if (res.data?.slug == "pixelfed") {
                            res.data.icon = R.drawable.pixelfed_logo
                        }
                        else if (res.data?.slug == "mastodon") {
                            res.data.icon = R.drawable.mastodon_logo
                        }
                        else if (res.data?.slug == "peertube") {
                            res.data.icon = R.drawable.peertube_logo
                        }
                        else if (res.data?.slug == "lemmy") {
                            res.data.icon = R.drawable.lemmy_logo
                        }
                        emit(res)
                    }
                }
    }
}

