package com.daniebeler.pfpixelix.data.remote.dto


import com.google.gson.annotations.SerializedName

data class FocusDto(
    @SerializedName("x") val x: Int, @SerializedName("y") val y: Int
)