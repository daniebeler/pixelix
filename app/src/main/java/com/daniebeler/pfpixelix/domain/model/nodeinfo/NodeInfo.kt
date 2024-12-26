package com.daniebeler.pfpixelix.domain.model.nodeinfo

data class NodeInfo(
    val software: String,
    val usage: NodeinfoUsage,
    val metadata: NodeinfoMetadata
)