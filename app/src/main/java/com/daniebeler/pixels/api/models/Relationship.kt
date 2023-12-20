package com.daniebeler.pixels.api.models

import com.google.gson.annotations.SerializedName

data class Relationship(
    @SerializedName("id")
    val id: String,
    @SerializedName("following")
    val following: Boolean,
    @SerializedName("followed_by")
    val followedBy: Boolean
)
