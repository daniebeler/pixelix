package com.daniebeler.pixels.api.models

import com.google.gson.annotations.SerializedName

data class Hashtag(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)
