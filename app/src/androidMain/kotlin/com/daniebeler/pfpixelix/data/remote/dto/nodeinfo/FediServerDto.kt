package com.daniebeler.pfpixelix.data.remote.dto.nodeinfo


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FediServerDto(
    @SerialName("banner_url")
    val bannerUrl: String?,
    @SerialName("description")
    val description: String?,
    @SerialName("domain")
    val domain: String?,
    @SerialName("id")
    val id: Int,
    @SerialName("open_registration")
    val openRegistration: Boolean?,
    @SerialName("software")
    val software: SoftwareSmallDto?,
    @SerialName("stats")
    val stats: ServerStatsDto?,
    @SerialName("location")
    val location: ServerLocationDto?
)