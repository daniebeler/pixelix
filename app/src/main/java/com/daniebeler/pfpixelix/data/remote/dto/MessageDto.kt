package com.daniebeler.pfpixelix.data.remote.dto

import com.daniebeler.pfpixelix.domain.model.Chat
import com.daniebeler.pfpixelix.domain.model.Message

data class MessageDto(
    val hidden: Boolean,
    val id: String,
    val isAuthor: Boolean,
    val media: Any,
    val meta: Any,
    val reportId: String,
    val seen: Boolean,
    val text: String,
    val timeAgo: String,
    val type: String
): DtoInterface<Message> {
    override fun toModel(): Message {
        return Message(
            hidden = hidden,
            id = id,
            isAuthor = isAuthor,
            reportId = reportId,
            seen = seen,
            text = text,
            timeAgo = timeAgo,
            type = type
        )
    }
}