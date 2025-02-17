package com.daniebeler.pfpixelix.data.remote.dto


import com.daniebeler.pfpixelix.domain.model.Account
import com.daniebeler.pfpixelix.domain.model.Notification
import com.daniebeler.pfpixelix.utils.TimeAgo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NotificationDto(
    @SerialName("account") var account: AccountDto?,
    @SerialName("created_at") val createdAt: String,
    @SerialName("id") val id: String,
    @SerialName("relationship") val relationship: RelationshipDto?,
    @SerialName("type") val type: String,
    @SerialName("status") val post: PostDto?
) : DtoInterface<Notification> {
    override fun toModel(): Notification {
        return Notification(
            account = account?.toModel() ?: Account(
                username = "",
                avatar = "",
                url = "",
                id = "",
                displayname = "",
                followersCount = 0,
                acct = "",
                note = "",
                locked = false,
                isAdmin = false,
                createdAt = "",
                postsCount = 0,
                followingCount = 0,
                website = null,
                pronouns = emptyList()
            ),
            id = id,
            type = type,
            post = post?.toModel(),
            createdAt = createdAt,
            timeAgo = TimeAgo.convertTimeToText(createdAt)
        )
    }
}