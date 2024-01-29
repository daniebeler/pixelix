package com.daniebeler.pixelix.data.remote.dto


import com.daniebeler.pixelix.domain.model.Notification
import com.daniebeler.pixelix.utils.TimeAgo
import com.google.gson.annotations.SerializedName

data class NotificationDto(
    @SerializedName("account")
    val account: AccountDto,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("relationship")
    val relationship: RelationshipDto,
    @SerializedName("type")
    val type: String,
    @SerializedName("status")
    val post: PostDto?
) : DtoInterface<Notification> {
    override fun toModel(): Notification {
        return Notification(
            account = account.toModel(),
            id = id,
            type = type,
            post = post?.toModel(),
            createdAt = createdAt,
            timeAgo = TimeAgo().covertTimeToText(createdAt)
        )
    }
}