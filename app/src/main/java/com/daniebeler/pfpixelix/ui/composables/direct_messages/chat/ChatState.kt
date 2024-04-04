package com.daniebeler.pfpixelix.ui.composables.direct_messages.chat

import com.daniebeler.pfpixelix.domain.model.Chat
import com.daniebeler.pfpixelix.domain.model.Conversation

data class ChatState(
    val isLoading: Boolean = false,
    val chat: Chat? = null,
    val error: String = ""
)
