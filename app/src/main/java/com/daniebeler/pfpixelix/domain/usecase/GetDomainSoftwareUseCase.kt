package com.daniebeler.pfpixelix.domain.usecase

import android.content.Context
import com.daniebeler.pfpixelix.R
import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.model.DomainSoftware
import com.daniebeler.pfpixelix.domain.repository.CountryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetDomainSoftwareUseCase(private val countryRepository: CountryRepository) {
    operator fun invoke(domain: String, context: Context): Flow<Resource<DomainSoftware>> = flow {
        if (domain == "threads.net") {
            val domainSoftware = DomainSoftware(
                name = "Threads",
                icon = R.drawable.threads_logo,
                link = "https://www.threads.net",
                description = context.resources.getString(R.string.threads_description)
            )
            emit(Resource.Success(domainSoftware))
        } else {
            emit(Resource.Loading())
            countryRepository.getWellKnownDomains("https://$domain/.well-known/nodeinfo")
                .collect { wellKnownDomains ->
                    if (wellKnownDomains is Resource.Error) {
                        emit(Resource.Error(wellKnownDomains.message!!))
                    }
                    if (wellKnownDomains is Resource.Success) {
                        if (wellKnownDomains.data?.links == null || wellKnownDomains.data.links.isEmpty()) {
                            emit(Resource.Error("an error occurred"))
                        } else {
                            wellKnownDomains.data.links[0].let {
                                countryRepository.getNodeInfo(it.href).collect { nodeInfo ->
                                    if (nodeInfo is Resource.Success && nodeInfo.data != null) {
                                        val res = getData(nodeInfo.data.software, context)
                                        if (res != null) {
                                            emit(Resource.Success(res))
                                        } else {
                                            emit(Resource.Error("not valid software"))
                                        }
                                    } else if (nodeInfo is Resource.Error) {
                                        emit(Resource.Error(nodeInfo.message!!))
                                    }
                                }
                            }
                        }
                    }
                }
        }
    }

    private fun getData(domainSoftware: String, context: Context): DomainSoftware? {
        return when (domainSoftware) {
            "pixelfed" -> {
                DomainSoftware(
                    name = "Pixelfed",
                    icon = R.drawable.pixelfed_logo,
                    link = "https://pixelfed.org/",
                    description = context.resources.getString(R.string.pixelfed_description)
                )
            }

            "mastodon" -> {
                DomainSoftware(
                    name = "Mastodon",
                    icon = R.drawable.mastodon_logo,
                    link = "https://joinmastodon.org/",
                    description = context.resources.getString(R.string.mastodon_description)
                )
            }

            "peertube" -> {
                DomainSoftware(
                    name = "PeerTube",
                    icon = R.drawable.peertube_logo,
                    link = "https://joinpeertube.org/",
                    description = context.resources.getString(R.string.peertube_description)
                )
            }

            "lemmy" -> {
                DomainSoftware(
                    name = "Lemmy",
                    icon = R.drawable.lemmy_logo,
                    link = "https://join-lemmy.org/",
                    description = context.resources.getString(R.string.lemmy_description)
                )
            }

            else -> {
                return null
            }
        }
    }
}

