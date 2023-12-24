package com.daniebeler.pixels.data.remote.dto


import com.google.gson.annotations.SerializedName

data class NotificationDto(
    @SerializedName("account")
    val account: AccountDto,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("relationship")
    val relationship: RelationshipDto,
    @SerializedName("type")
    val type: String
)