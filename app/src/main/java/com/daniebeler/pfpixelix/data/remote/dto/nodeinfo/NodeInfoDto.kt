package com.daniebeler.pfpixelix.data.remote.dto.nodeinfo

import com.daniebeler.pfpixelix.data.remote.dto.DomainSoftwareDto
import com.daniebeler.pfpixelix.data.remote.dto.DtoInterface
import com.daniebeler.pfpixelix.domain.model.nodeinfo.NodeInfo
import kotlinx.serialization.Serializable

@Serializable
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
