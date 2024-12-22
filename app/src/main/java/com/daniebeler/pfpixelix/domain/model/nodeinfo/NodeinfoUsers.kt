package com.daniebeler.pfpixelix.domain.model.nodeinfo


import com.google.gson.annotations.SerializedName

data class NodeinfoUsers(
    @SerializedName("activeHalfyear")
    val activeHalfyear: Int,
    @SerializedName("activeMonth")
    val activeMonth: Int,
    @SerializedName("total")
    val total: Int
)