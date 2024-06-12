package com.daniebeler.pfpixelix.domain.model

import com.google.gson.annotations.SerializedName

data class Story(
    val createdAt: String,
    val duration: Int,
    val id: String,
    val seen: Boolean,
    val src: String,
    val type: String
)
