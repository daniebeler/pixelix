package com.daniebeler.pfpixelix.domain.model

data class Notification(
    val account: Account,
    val id: String,
    val type: String,
    val post: Post?,
    var createdAt: String,
    var timeAgo: String
)