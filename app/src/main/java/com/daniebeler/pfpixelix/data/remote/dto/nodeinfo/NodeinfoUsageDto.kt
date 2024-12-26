package com.daniebeler.pfpixelix.data.remote.dto.nodeinfo


import com.daniebeler.pfpixelix.data.remote.dto.DtoInterface
import com.daniebeler.pfpixelix.domain.model.nodeinfo.NodeinfoUsage
import com.daniebeler.pfpixelix.domain.model.nodeinfo.NodeinfoUsers
import com.google.gson.annotations.SerializedName

data class NodeinfoUsageDto(
    @SerializedName("localComments") val localComments: Int?,
    @SerializedName("localPosts") val localPosts: Int?,
    @SerializedName("users") val users: NodeinfoUsersDto?
) : DtoInterface<NodeinfoUsage> {
    override fun toModel(): NodeinfoUsage {
        return NodeinfoUsage(
            localComments = localComments ?: -1,
            localPosts = localPosts ?: -1,
            users = users?.toModel() ?: NodeinfoUsers(-1, -1, -1)
        )
    }
}