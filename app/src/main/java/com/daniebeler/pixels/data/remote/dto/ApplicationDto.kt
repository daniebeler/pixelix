package com.daniebeler.pixels.data.remote.dto


import com.google.gson.annotations.SerializedName

data class ApplicationDto(
    @SerializedName("name")
    val name: String,
    @SerializedName("website")
    val website: Any,
    @SerializedName("id")
    val id: String,
    @SerializedName("redirect_uri")
    val redirectUri: String,
    @SerializedName("client_id")
    val clientId: String,
    @SerializedName("client_secret")
    val clientSecret: String
)