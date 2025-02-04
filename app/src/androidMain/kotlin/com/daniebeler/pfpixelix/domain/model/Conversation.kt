package com.daniebeler.pfpixelix.domain.model

class Conversation (
    val id: Int,
    val unread: Boolean,
    val accounts: List<Account>,
    val lastPost: Post
)