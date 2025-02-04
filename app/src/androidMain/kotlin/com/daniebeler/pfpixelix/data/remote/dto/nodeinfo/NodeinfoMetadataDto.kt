package com.daniebeler.pfpixelix.data.remote.dto.nodeinfo


import com.daniebeler.pfpixelix.data.remote.dto.DtoInterface
import com.daniebeler.pfpixelix.domain.model.nodeinfo.NodeinfoMetadata
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NodeinfoMetadataDto(
    @SerialName("nodeDescription") val nodeDescription: String?,
    @SerialName("nodeName") val nodeName: String?
): DtoInterface<NodeinfoMetadata> {
    override fun toModel(): NodeinfoMetadata {
        return NodeinfoMetadata(
            nodeDescription ?: "",
            nodeName ?: ""
        )
    }
}