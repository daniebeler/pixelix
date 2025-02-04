package com.daniebeler.pfpixelix.domain.usecase

import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.model.Conversation
import com.daniebeler.pfpixelix.domain.repository.DirectMessagesRepository
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class GetConversationsUseCase(
    private val directMessagesRepository: DirectMessagesRepository
) {
    operator fun invoke(): Flow<Resource<List<Conversation>>> {
        return directMessagesRepository.getConversations()
    }
}