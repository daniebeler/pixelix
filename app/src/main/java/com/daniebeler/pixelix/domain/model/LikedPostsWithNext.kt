package com.daniebeler.pixelix.domain.model

data class LikedPostsWithNext(
    val posts: List<Post> = emptyList(),
    val nextId: String = ""
)
