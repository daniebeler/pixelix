package com.daniebeler.pfpixelix.domain.model.nodeinfo


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NodeinfoMetadata(
    @SerialName("nodeDescription")
    val nodeDescription: String,
    @SerialName("nodeName")
    val nodeName: String
)