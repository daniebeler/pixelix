package com.daniebeler.pixels.api.models

import com.google.gson.annotations.SerializedName

data class Application(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("redirect_uri")
    val redirectUri: String,
    @SerializedName("client_id")
    val clientId: String,
    @SerializedName("client_secret")
    val clientSecret: String
)
