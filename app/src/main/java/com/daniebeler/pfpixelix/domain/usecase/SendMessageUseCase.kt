package com.daniebeler.pfpixelix.domain.usecase

import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.data.remote.dto.CreateMessageDto
import com.daniebeler.pfpixelix.domain.model.Conversation
import com.daniebeler.pfpixelix.domain.model.Message
import com.daniebeler.pfpixelix.domain.repository.DirectMessagesRepository
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class SendMessageUseCase(
    private val directMessagesRepository: DirectMessagesRepository
) {
    operator fun invoke(createMessageDto: CreateMessageDto): Flow<Resource<Message>> {
        return directMessagesRepository.sendMessage(createMessageDto)
    }
}