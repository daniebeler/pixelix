package com.daniebeler.pfpixelix.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Account(
    val id: String,
    val username: String,
    val acct: String,
    val displayname: String?,
    val avatar: String,
    val followersCount: Int,
    val followingCount: Int,
    val postsCount: Int,
    val website: String?,
    val note: String,
    val url: String,
    val locked: Boolean,
    val createdAt: String,
    val isAdmin: Boolean,
    val pronouns: List<String>
)
