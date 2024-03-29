package com.daniebeler.pfpixelix.data.remote.dto

import com.daniebeler.pfpixelix.domain.model.AccessToken
import com.daniebeler.pfpixelix.domain.model.DomainSoftware

data class DomainSoftwareDto(
    val name: String,
    val version: String
) : DtoInterface<DomainSoftware> {
    override fun toModel(): DomainSoftware {
        return DomainSoftware(
            name,
            version,
            icon = null
        )
    }
}
