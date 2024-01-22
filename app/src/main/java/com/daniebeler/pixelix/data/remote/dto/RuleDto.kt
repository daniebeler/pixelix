package com.daniebeler.pixelix.data.remote.dto

import com.google.gson.annotations.SerializedName

data class RuleDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("text")
    val text: String
)
