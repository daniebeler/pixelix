package com.daniebeler.pixels.data.remote.dto


import com.google.gson.annotations.SerializedName

data class TagDto(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)