package com.daniebeler.pfpixelix.data.remote.dto

import com.daniebeler.pfpixelix.domain.model.NodeInfo

data class NodeInfoDto(
    val software: DomainSoftwareDto
) : DtoInterface<NodeInfo> {
    override fun toModel(): NodeInfo {
        return NodeInfo(
            software.toModel()
        )
    }
}
