package com.daniebeler.pixels.domain.model

import com.daniebeler.pixels.data.remote.dto.RelationshipDto
import com.google.gson.annotations.SerializedName

data class Relationship(
    val id: String,
    val following: Boolean,
    val followedBy: Boolean
)

fun RelationshipDto.toRelationship(): Relationship {
    return Relationship(
        id = id,
        following = following,
        followedBy = followedBy
    )
}