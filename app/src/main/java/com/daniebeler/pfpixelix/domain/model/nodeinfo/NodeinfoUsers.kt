package com.daniebeler.pfpixelix.domain.model.nodeinfo


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NodeinfoUsers(
    @SerialName("activeHalfyear")
    val activeHalfyear: Int,
    @SerialName("activeMonth")
    val activeMonth: Int,
    @SerialName("total")
    val total: Int
)