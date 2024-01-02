package com.daniebeler.pixelix.data.remote.dto


import com.google.gson.annotations.SerializedName

data class TagDto(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("following")
    val following: Boolean,
    @SerializedName("count")
    val count: Int
)