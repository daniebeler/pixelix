package com.daniebeler.pfpixelix.domain.model
import androidx.annotation.DrawableRes

data class DomainSoftware(
    val domain: String,
    val name: String,
    @DrawableRes
    val icon: Int,
    val link: String,
    val description: String,
    var totalUserCount: Int = -1,
    var activeUserCount: Int = -1,
    var postsCount: Int = -1,
    var nodeDescription: String = ""
)