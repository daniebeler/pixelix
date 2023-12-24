package com.daniebeler.pixels.data.remote.dto


import com.google.gson.annotations.SerializedName

data class MentionDto(
    @SerializedName("acct")
    val acct: String,
    @SerializedName("avatar")
    val avatar: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("discoverable")
    val discoverable: Boolean,
    @SerializedName("display_name")
    val displayName: String,
    @SerializedName("followers_count")
    val followersCount: Int,
    @SerializedName("following_count")
    val followingCount: Int,
    @SerializedName("header_bg")
    val headerBg: Any,
    @SerializedName("id")
    val id: String,
    @SerializedName("is_admin")
    val isAdmin: Boolean,
    @SerializedName("last_fetched_at")
    val lastFetchedAt: String,
    @SerializedName("local")
    val local: Boolean,
    @SerializedName("location")
    val location: Any,
    @SerializedName("locked")
    val locked: Boolean,
    @SerializedName("note")
    val note: String,
    @SerializedName("note_text")
    val noteText: String,
    @SerializedName("pronouns")
    val pronouns: List<Any>,
    @SerializedName("statuses_count")
    val statusesCount: Int,
    @SerializedName("url")
    val url: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("website")
    val website: Any
)