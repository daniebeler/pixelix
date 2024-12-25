package com.daniebeler.pfpixelix.data.remote.dto.nodeinfo

import com.daniebeler.pfpixelix.data.remote.dto.DtoInterface
import com.daniebeler.pfpixelix.domain.model.nodeinfo.FediServer

data class WrapperDto(
    val data: FediServerDto
) : DtoInterface<FediServer> {
    override fun toModel(): FediServer {
        return FediServer(
            bannerUrl = data.bannerUrl,
            description = data.description,
            domain = data.domain,
            id = data.id,
            openRegistration = data.openRegistration,
            software = data.software.toModel()
        )
    }
}
