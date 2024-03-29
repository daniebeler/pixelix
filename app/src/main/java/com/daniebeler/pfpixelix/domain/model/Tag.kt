package com.daniebeler.pfpixelix.domain.model

data class Tag(
    var name: String,
    val url: String,
    var following: Boolean,
    var count: Int?,
    val total: Int,
    val hashtag: String?
)