package com.daniebeler.pfpixelix.data.repository

import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.data.remote.PixelfedApi
import com.daniebeler.pfpixelix.data.remote.dto.ChatDto
import com.daniebeler.pfpixelix.data.remote.dto.ConversationDto
import com.daniebeler.pfpixelix.data.remote.dto.TagDto
import com.daniebeler.pfpixelix.domain.model.Chat
import com.daniebeler.pfpixelix.domain.model.Conversation
import com.daniebeler.pfpixelix.domain.model.Tag
import com.daniebeler.pfpixelix.domain.repository.DirectMessagesRepository
import com.daniebeler.pfpixelix.domain.repository.HashtagRepository
import com.daniebeler.pfpixelix.utils.NetworkCall
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DirectMessagesRepositoryImpl @Inject constructor(
    private val pixelfedApi: PixelfedApi
) : DirectMessagesRepository {

    override fun getConversations(): Flow<Resource<List<Conversation>>> {
        return NetworkCall<Conversation, ConversationDto>().makeCallList(pixelfedApi.getConversations())
    }

    override fun getChat(accountId: String): Flow<Resource<Chat>> {
        return NetworkCall<Chat, ChatDto>().makeCall(pixelfedApi.getChat(accountId))
    }
}