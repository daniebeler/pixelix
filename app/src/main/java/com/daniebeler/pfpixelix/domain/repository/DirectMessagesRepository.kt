package com.daniebeler.pfpixelix.domain.repository

import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.data.remote.dto.CreateMessageDto
import com.daniebeler.pfpixelix.domain.model.Chat
import com.daniebeler.pfpixelix.domain.model.Conversation
import com.daniebeler.pfpixelix.domain.model.Message
import com.daniebeler.pfpixelix.domain.model.Tag
import kotlinx.coroutines.flow.Flow

interface DirectMessagesRepository {

    fun getConversations(): Flow<Resource<List<Conversation>>>
    fun getChat(accountId: String, maxId: String = ""): Flow<Resource<Chat>>
    fun sendMessage(createMessageDto: CreateMessageDto): Flow<Resource<Message>>
    fun deleteMessage(id: String): Flow<Resource<List<Int>>>
}