package com.daniebeler.pfpixelix.data.remote.dto


import com.daniebeler.pfpixelix.domain.model.Notification
import com.daniebeler.pfpixelix.utils.TimeAgo
import com.google.gson.annotations.SerializedName

data class NotificationDto(
    @SerializedName("account") val account: AccountDto,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("id") val id: String,
    @SerializedName("relationship") val relationship: RelationshipDto,
    @SerializedName("type") val type: String,
    @SerializedName("status") val post: PostDto?
) : DtoInterface<Notification> {
    override fun toModel(): Notification {
        return Notification(
            account = account.toModel(),
            id = id,
            type = type,
            post = post?.toModel(),
            createdAt = createdAt,
            timeAgo = TimeAgo.convertTimeToText(createdAt)
        )
    }
}