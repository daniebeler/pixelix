package com.daniebeler.pfpixelix.domain.model

data class StoryUser(
    val avatar: String,
    val id: String,
    val isAuthor: Boolean,
    val local: Boolean,
    val username: String
)
