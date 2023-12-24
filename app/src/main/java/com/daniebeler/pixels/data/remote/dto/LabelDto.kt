package com.daniebeler.pixels.data.remote.dto


import com.google.gson.annotations.SerializedName

data class LabelDto(
    @SerializedName("covid")
    val covid: Boolean
)