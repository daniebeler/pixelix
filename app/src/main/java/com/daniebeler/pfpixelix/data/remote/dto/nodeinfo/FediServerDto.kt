package com.daniebeler.pfpixelix.data.remote.dto.nodeinfo


import com.google.gson.annotations.SerializedName

data class FediServerDto(
    @SerializedName("banner_url")
    val bannerUrl: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("domain")
    val domain: String?,
    @SerializedName("id")
    val id: Int,
    @SerializedName("open_registration")
    val openRegistration: Boolean?,
    @SerializedName("software")
    val software: SoftwareSmallDto?,
    @SerializedName("stats")
    val stats: ServerStatsDto?,
    @SerializedName("location")
    val location: ServerLocationDto?
)