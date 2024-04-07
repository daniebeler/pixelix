package com.daniebeler.pfpixelix.data.remote.dto

import com.daniebeler.pfpixelix.domain.model.Conversation
import com.google.gson.annotations.SerializedName

data class ConversationDto(
    val id: Int,
    val unread: Boolean,
    val accounts: List<AccountDto>,
    @SerializedName("last_status") val lastPost: PostDto
) : DtoInterface<Conversation> {
    override fun toModel(): Conversation {
        return Conversation(
            id = id,
            unread = unread,
            accounts = accounts.map { it.toModel() },
            lastPost = lastPost.toModel()
        )
    }
}
