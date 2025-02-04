package com.daniebeler.pfpixelix.data.remote.dto

import com.daniebeler.pfpixelix.domain.model.WellKnownDomains
import kotlinx.serialization.Serializable

@Serializable
data class WellKnownDomainsDto(
    val links: List<LinkDto>
) : DtoInterface<WellKnownDomains> {
    override fun toModel(): WellKnownDomains {
        return WellKnownDomains(links = links.map { it.toModel() })
    }
}