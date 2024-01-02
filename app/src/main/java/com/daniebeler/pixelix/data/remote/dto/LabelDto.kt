package com.daniebeler.pixelix.data.remote.dto


import com.google.gson.annotations.SerializedName

data class LabelDto(
    @SerializedName("covid")
    val covid: Boolean
)