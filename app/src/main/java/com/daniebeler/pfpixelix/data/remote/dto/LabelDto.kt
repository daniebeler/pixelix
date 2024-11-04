package com.daniebeler.pfpixelix.data.remote.dto


import com.google.gson.annotations.SerializedName

data class LabelDto(
    @SerializedName("covid") val covid: Boolean
)