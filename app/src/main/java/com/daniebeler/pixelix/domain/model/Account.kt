package com.daniebeler.pixelix.domain.model

import com.daniebeler.pixelix.data.remote.dto.AccountDto

data class Account(
    val id: String,
    val username: String,
    val acct: String,
    val displayname: String,
    val avatar: String,
    val followersCount: Int,
    val followingCount: Int,
    val postsCount: Int,
    val website: String?,
    val note: String,
    val url: String
)
