package com.daniebeler.pfpixelix.domain.model

data class StoryWrapper(
    val id: String,
    val nodes: List<Story>,
    val seen: Boolean,
    val url: String,
    val user: StoryUser
)
