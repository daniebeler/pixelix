package com.daniebeler.pfpixelix.data.remote.dto.nodeinfo


import com.daniebeler.pfpixelix.data.remote.dto.DtoInterface
import com.daniebeler.pfpixelix.domain.model.nodeinfo.NodeinfoUsers
import com.google.gson.annotations.SerializedName

data class NodeinfoUsersDto(
    @SerializedName("activeHalfyear") val activeHalfyear: Int?,
    @SerializedName("activeMonth") val activeMonth: Int?,
    @SerializedName("total") val total: Int?
) : DtoInterface<NodeinfoUsers> {
    override fun toModel(): NodeinfoUsers {
        return NodeinfoUsers(
            activeHalfyear = activeHalfyear ?: -1,
            activeMonth = activeMonth ?: -1,
            total = total ?: -1
        )
    }
}