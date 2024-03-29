package com.daniebeler.pfpixelix.domain.usecase

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.EmojiEmotions
import androidx.compose.material.icons.outlined.EmojiObjects
import androidx.compose.material.icons.outlined.VideoCameraFront
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.daniebeler.pfpixelix.R
import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.model.DomainSoftware
import com.daniebeler.pfpixelix.domain.model.Post
import com.daniebeler.pfpixelix.domain.repository.CountryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Retrofit

class GetDomainSoftwareUseCase(private val countryRepository: CountryRepository) {
    operator fun invoke(domain: String, context: Context): Flow<Resource<DomainSoftware>> = flow {
        emit(Resource.Loading())
        countryRepository.getWellKnownDomains("https://$domain/.well-known/nodeinfo").collect { wellKnownDomains ->
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
                                var apiData: DomainSoftware = nodeInfo.data.software
                                val res = getData(apiData, context)
                                emit(Resource.Success(res))
                            } else if (nodeInfo is Resource.Error) {
                                emit(Resource.Error(nodeInfo.message!!))
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getData(domainSoftware: DomainSoftware, context: Context): DomainSoftware {
        return when (domainSoftware.name) {
            "pixelfed" -> {
                domainSoftware.copy(icon = R.drawable.pixelfed_logo)
            }

            "mastodon" -> {
                domainSoftware.copy(icon = R.drawable.mastodon_logo)
            }

            "lemmy" -> {
                domainSoftware.copy(icon = R.drawable.lemmy_logo)
            }

            else -> {
                return domainSoftware
            }
        }
    }
}

