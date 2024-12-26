package com.daniebeler.pfpixelix.domain.model.nodeinfo


import com.google.gson.annotations.SerializedName

data class NodeinfoMetadata(
    @SerializedName("nodeDescription")
    val nodeDescription: String,
    @SerializedName("nodeName")
    val nodeName: String
)