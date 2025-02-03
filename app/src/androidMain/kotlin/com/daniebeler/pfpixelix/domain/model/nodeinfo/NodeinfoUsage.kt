package com.daniebeler.pfpixelix.domain.model.nodeinfo

data class NodeinfoUsage(
    val localComments: Int,
    val localPosts: Int,
    val users: NodeinfoUsers
)