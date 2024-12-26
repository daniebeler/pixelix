package com.daniebeler.pfpixelix.domain.model.nodeinfo

data class FediServer(
    val bannerUrl: String,
    val description: String,
    val domain: String,
    val id: Int,
    val openRegistration: Boolean,
    val software: SoftwareSmall,
    val stats: ServerStats
)