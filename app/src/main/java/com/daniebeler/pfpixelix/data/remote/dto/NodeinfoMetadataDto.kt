package com.daniebeler.pfpixelix.data.remote.dto


import com.daniebeler.pfpixelix.domain.model.nodeinfo.NodeinfoMetadata
import com.google.gson.annotations.SerializedName

data class NodeinfoMetadataDto(
    @SerializedName("nodeDescription")
    val nodeDescription: String?,
    @SerializedName("nodeName")
    val nodeName: String?
): DtoInterface<NodeinfoMetadata> {
    override fun toModel(): NodeinfoMetadata {
        return NodeinfoMetadata(
            nodeDescription ?: "",
            nodeName ?: ""
        )
    }
}