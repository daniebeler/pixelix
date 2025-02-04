package com.daniebeler.pfpixelix.data.remote.dto.nodeinfo


import com.daniebeler.pfpixelix.data.remote.dto.DtoInterface
import com.daniebeler.pfpixelix.domain.model.nodeinfo.NodeinfoUsers
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NodeinfoUsersDto(
    @SerialName("activeHalfyear") val activeHalfyear: Int?,
    @SerialName("activeMonth") val activeMonth: Int?,
    @SerialName("total") val total: Int?
) : DtoInterface<NodeinfoUsers> {
    override fun toModel(): NodeinfoUsers {
        return NodeinfoUsers(
            activeHalfyear = activeHalfyear ?: 0,
            activeMonth = activeMonth ?: 0,
            total = total ?: 0
        )
    }
}