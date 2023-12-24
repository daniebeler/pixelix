package com.daniebeler.pixels.data.remote.dto


import com.google.gson.annotations.SerializedName

data class RelationshipDto(
    @SerializedName("blocking")
    val blocking: Boolean,
    @SerializedName("domain_blocking")
    val domainBlocking: Any,
    @SerializedName("endorsed")
    val endorsed: Boolean,
    @SerializedName("followed_by")
    val followedBy: Boolean,
    @SerializedName("following")
    val following: Boolean,
    @SerializedName("id")
    val id: String,
    @SerializedName("muting")
    val muting: Boolean,
    @SerializedName("muting_notifications")
    val mutingNotifications: Any,
    @SerializedName("requested")
    val requested: Boolean,
    @SerializedName("showing_reblogs")
    val showingReblogs: Any
)