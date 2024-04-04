package com.daniebeler.pfpixelix.domain.usecase

import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.model.Chat
import com.daniebeler.pfpixelix.domain.model.Conversation
import com.daniebeler.pfpixelix.domain.repository.DirectMessagesRepository
import kotlinx.coroutines.flow.Flow

class GetChatUseCase(
    private val directMessagesRepository: DirectMessagesRepository
) {
    operator fun invoke(accountId: String): Flow<Resource<Chat>> {
        return directMessagesRepository.getChat(accountId)
    }
}