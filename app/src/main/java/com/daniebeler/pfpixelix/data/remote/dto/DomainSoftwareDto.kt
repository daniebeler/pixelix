package com.daniebeler.pfpixelix.data.remote.dto

import com.daniebeler.pfpixelix.domain.model.AccessToken
import com.daniebeler.pfpixelix.domain.model.DomainSoftware

data class DomainSoftwareDto(
    val name: String,
    val version: String
) : DtoInterface<String> {
    override fun toModel(): String {
        return name
    }
}
