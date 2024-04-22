package com.daniebeler.pfpixelix.data.repository

import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.data.remote.PixelfedApi
import com.daniebeler.pfpixelix.data.remote.dto.ChatDto
import com.daniebeler.pfpixelix.data.remote.dto.ConversationDto
import com.daniebeler.pfpixelix.data.remote.dto.CreateMessageDto
import com.daniebeler.pfpixelix.data.remote.dto.MessageDto
import com.daniebeler.pfpixelix.data.remote.dto.TagDto
import com.daniebeler.pfpixelix.domain.model.Chat
import com.daniebeler.pfpixelix.domain.model.Conversation
import com.daniebeler.pfpixelix.domain.model.Message
import com.daniebeler.pfpixelix.domain.model.Tag
import com.daniebeler.pfpixelix.domain.repository.DirectMessagesRepository
import com.daniebeler.pfpixelix.domain.repository.HashtagRepository
import com.daniebeler.pfpixelix.utils.NetworkCall
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.awaitResponse
import javax.inject.Inject

class DirectMessagesRepositoryImpl @Inject constructor(
    private val pixelfedApi: PixelfedApi
) : DirectMessagesRepository {

    override fun getConversations(): Flow<Resource<List<Conversation>>> {
        return NetworkCall<Conversation, ConversationDto>().makeCallList(pixelfedApi.getConversations())
    }

    override fun getChat(accountId: String, maxId: String): Flow<Resource<Chat>> {
        return if (maxId.isEmpty()) {
            NetworkCall<Chat, ChatDto>().makeCall(pixelfedApi.getChat(accountId))
        } else {
            NetworkCall<Chat, ChatDto>().makeCall(pixelfedApi.getChat(accountId, maxId))
        }
    }

    override fun sendMessage(createMessageDto: CreateMessageDto): Flow<Resource<Message>> {
        return NetworkCall<Message, MessageDto>().makeCall(pixelfedApi.sendMessage(createMessageDto))
    }

    override fun deleteMessage(id: String): Flow<Resource<List<Int>>> = flow {
        try {
            emit(Resource.Loading())
            val response = pixelfedApi.deleteMessage(id).awaitResponse()
            if (response.isSuccessful) {
                val res = response.body()
                emit(Resource.Success(res!!))
            } else {
                emit(Resource.Error("Unknown Error"))
            }
        } catch (exception: Exception) {
            emit(Resource.Error(exception.message ?: "Unknown Error"))
        }
    }
}