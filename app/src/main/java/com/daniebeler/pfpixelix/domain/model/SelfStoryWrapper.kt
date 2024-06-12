package com.daniebeler.pfpixelix.domain.model

data class SelfStoryWrapper(
    val nodes: List<Story>,
    val user: StoryUser
)
