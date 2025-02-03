package com.daniebeler.pfpixelix.data.remote.dto

import com.daniebeler.pfpixelix.domain.model.Chat
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class ChatDto(
    val avatar: String,
    val domain: JsonElement?,
    val id: String,
    val isLocal: Boolean,
    val messages: List<MessageDto>,
    val muted: Boolean,
    val name: String,
    val timeAgo: String,
    val url: String,
    val username: String
) : DtoInterface<Chat> {
    override fun toModel(): Chat {
        return Chat(
            avatar = avatar,
            id = id,
            isLocal = isLocal,
            messages = messages.map { it.toModel() },
            muted = muted,
            name = name,
            timeAgo = timeAgo,
            url = url,
            username = username
        )
    }
}