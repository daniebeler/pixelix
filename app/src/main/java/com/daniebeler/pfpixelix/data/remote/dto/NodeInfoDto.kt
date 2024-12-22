package com.daniebeler.pfpixelix.data.remote.dto

import com.daniebeler.pfpixelix.domain.model.nodeinfo.NodeInfo

data class NodeInfoDto(
    val software: DomainSoftwareDto,
    val usage: NodeinfoUsageDto,
    val metadata: NodeinfoMetadataDto
) : DtoInterface<NodeInfo> {
    override fun toModel(): NodeInfo {
        return NodeInfo(
            software.toModel(),
            usage.toModel(),
            metadata = metadata.toModel()
        )
    }
}
