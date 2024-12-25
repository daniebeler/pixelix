package com.daniebeler.pfpixelix.domain.model.nodeinfo

import androidx.annotation.DrawableRes

data class FediSoftware(
    val description: String,
    val id: Int,
    val instanceCount: Int,
    val license: String,
    val name: String,
    val slug: String,
    val statusCount: Int,
    val userCount: Int,
    @DrawableRes
    var icon: Int?,
)