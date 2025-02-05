package com.daniebeler.pfpixelix.domain.model.nodeinfo

import org.jetbrains.compose.resources.DrawableResource

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
    var icon: DrawableResource?,
    val website: String
)