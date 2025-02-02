package com.daniebeler.pfpixelix.data.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FocusDto(
    @SerialName("x") val x: Int,
    @SerialName("y") val y: Int
)