package com.daniebeler.pfpixelix.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UpdateAccountDto (
    @SerializedName("display_name")
    val displayName: String,
    val note: String,
    val website: String
)