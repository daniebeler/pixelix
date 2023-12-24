package com.daniebeler.pixels.data.remote.dto


import com.google.gson.annotations.SerializedName

data class OriginalDto(
    @SerializedName("aspect")
    val aspect: Double,
    @SerializedName("height")
    val height: Int,
    @SerializedName("size")
    val size: String,
    @SerializedName("width")
    val width: Int
)