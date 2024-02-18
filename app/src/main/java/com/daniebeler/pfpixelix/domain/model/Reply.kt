package com.daniebeler.pfpixelix.domain.model

data class Reply(
    val id: String,
    val content: String,
    val mentions: List<Account>,
    val account: Account,
    val createdAt: String
)