package com.daniebeler.pixels.data.remote.dto


import com.google.gson.annotations.SerializedName

data class ApplicationDto(
    @SerializedName("name")
    val name: String,
    @SerializedName("website")
    val website: Any
)