package com.daniebeler.pixels.api.models

import com.google.gson.annotations.SerializedName

data class Notification(
    @SerializedName("type")
    val type: String,
    @SerializedName("account")
    val account: Account,
    @SerializedName("status")
    val post: Post?
)
