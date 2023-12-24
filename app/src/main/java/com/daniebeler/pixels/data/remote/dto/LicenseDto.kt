package com.daniebeler.pixels.data.remote.dto


import com.google.gson.annotations.SerializedName

data class LicenseDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("url")
    val url: String
)