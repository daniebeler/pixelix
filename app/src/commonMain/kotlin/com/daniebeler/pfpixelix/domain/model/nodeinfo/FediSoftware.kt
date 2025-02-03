package com.daniebeler.pfpixelix.domain.model.nodeinfo

data class FediSoftware(
    val description: String,
    val id: Int,
    val instanceCount: Int,
    val license: String,
    val name: String,
    val slug: String,
    val statusCount: Int,
    val userCount: Int,
    val activeUserCount: Int,
    var icon: Int?, //todo KMP
    val website: String
)