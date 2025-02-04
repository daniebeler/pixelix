package com.daniebeler.pfpixelix.data.remote.dto.nodeinfo


import com.daniebeler.pfpixelix.data.remote.dto.DtoInterface
import com.daniebeler.pfpixelix.domain.model.nodeinfo.NodeinfoUsage
import com.daniebeler.pfpixelix.domain.model.nodeinfo.NodeinfoUsers
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NodeinfoUsageDto(
    @SerialName("localComments") val localComments: Int?,
    @SerialName("localPosts") val localPosts: Int?,
    @SerialName("users") val users: NodeinfoUsersDto?
) : DtoInterface<NodeinfoUsage> {
    override fun toModel(): NodeinfoUsage {
        return NodeinfoUsage(
            localComments = localComments ?: 0,
            localPosts = localPosts ?: 0,
            users = users?.toModel() ?: NodeinfoUsers(-1, -1, -1)
        )
    }
}