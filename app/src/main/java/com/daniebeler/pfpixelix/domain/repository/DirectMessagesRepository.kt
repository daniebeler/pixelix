package com.daniebeler.pfpixelix.domain.repository

import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.model.Conversation
import com.daniebeler.pfpixelix.domain.model.Tag
import kotlinx.coroutines.flow.Flow

interface DirectMessagesRepository {

    fun getConversations(): Flow<Resource<List<Conversation>>>
}