package com.daniebeler.pfpixelix.data.remote.dto

import com.daniebeler.pfpixelix.domain.model.LikedBy
import com.daniebeler.pfpixelix.domain.model.WellKnownDomains

data class WellKnownDomainsDto(
    val links: List<LinkDto>
) : DtoInterface<WellKnownDomains> {
    override fun toModel(): WellKnownDomains {
        return WellKnownDomains(
            links = links.map { it.toModel() }
        )
    }
}