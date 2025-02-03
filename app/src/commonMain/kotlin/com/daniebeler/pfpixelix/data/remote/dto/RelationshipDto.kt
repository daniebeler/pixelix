package com.daniebeler.pfpixelix.data.remote.dto

import com.daniebeler.pfpixelix.domain.model.Relationship
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class RelationshipDto(
    @SerialName("blocking") val blocking: Boolean,
    @SerialName("domain_blocking") val domainBlocking: JsonElement?,
    @SerialName("endorsed") val endorsed: Boolean?,
    @SerialName("followed_by") val followedBy: Boolean,
    @SerialName("following") val following: Boolean,
    @SerialName("id") val id: String,
    @SerialName("muting") val muting: Boolean,
    @SerialName("muting_notifications") val mutingNotifications: JsonElement?,
    @SerialName("requested") val requested: Boolean?,
    @SerialName("showing_reblogs") val showingReblogs: JsonElement?
) : DtoInterface<Relationship> {
    override fun toModel(): Relationship {
        return Relationship(
            id = id,
            following = following,
            followedBy = followedBy,
            muting = muting,
            blocking = blocking
        )
    }
}