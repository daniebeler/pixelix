package com.daniebeler.pfpixelix.data.remote.dto

import com.daniebeler.pfpixelix.domain.model.Message
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class MessageDto(
    val hidden: Boolean,
    val id: String,
    val isAuthor: Boolean,
    val media: JsonElement?,
    val meta: JsonElement?,
    val reportId: String,
    val seen: Boolean,
    val text: String?,
    val timeAgo: String,
    val type: String
) : DtoInterface<Message> {
    override fun toModel(): Message {
        return Message(
            hidden = hidden,
            id = id,
            isAuthor = isAuthor,
            reportId = reportId,
            seen = seen,
            text = text ?: "",
            timeAgo = timeAgo,
            type = type
        )
    }
}