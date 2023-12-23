package com.daniebeler.pixels.domain.model

import com.daniebeler.pixels.data.remote.dto.AccountDto

data class Account(
    val id: String,
    val username: String,
    val acct: String,
    val displayname: String,
    val avatar: String,
    val followersCount: Int,
    val followingCount: Int,
    val postsCount: Int,
    val website: Any,
    val note: String,
    val url: String
)

fun AccountDto.toAccount(): Account {
    return Account(
        id = id,
        username = username,
        acct = acct,
        displayname = displayName,
        avatar = avatar,
        followersCount = followersCount,
        followingCount = followingCount,
        postsCount = statusesCount,
        website = website,
        note = note,
        url = url
    )
}