package com.daniebeler.pixels.api.models

import com.google.gson.annotations.SerializedName

data class Account(
    @SerializedName("id")
    val id: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("acct")
    val acct: String,
    @SerializedName("display_name")
    val displayname: String,
    @SerializedName("avatar")
    val avatar: String,
    @SerializedName("followers_count")
    val followersCount: Int,
    @SerializedName("following_count")
    val followingCount: Int,
    @SerializedName("statuses_count")
    val postsCount: Int,
    @SerializedName("website")
    val website: String,
    @SerializedName("note_text")
    val note: String,
    @SerializedName("url")
    val url: String
)
