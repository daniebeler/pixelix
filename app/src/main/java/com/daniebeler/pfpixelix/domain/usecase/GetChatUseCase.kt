package com.daniebeler.pfpixelix.domain.usecase

import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.model.Chat
import com.daniebeler.pfpixelix.domain.model.Conversation
import com.daniebeler.pfpixelix.domain.repository.DirectMessagesRepository
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class GetChatUseCase(
    private val directMessagesRepository: DirectMessagesRepository
) {
    operator fun invoke(accountId: String, maxChatId: String = ""): Flow<Resource<Chat>> {
        return directMessagesRepository.getChat(accountId, maxChatId)
    }
}