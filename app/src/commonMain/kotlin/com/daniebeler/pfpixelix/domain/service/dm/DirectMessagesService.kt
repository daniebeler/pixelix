package com.daniebeler.pfpixelix.domain.service.dm

import com.daniebeler.pfpixelix.data.remote.PixelfedApi
import com.daniebeler.pfpixelix.data.remote.dto.CreateMessageDto
import com.daniebeler.pfpixelix.domain.service.utils.loadListResources
import com.daniebeler.pfpixelix.domain.service.utils.loadResource
import com.daniebeler.pfpixelix.domain.service.utils.loadType
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Inject

@Inject
class DirectMessagesService(
    private val api: PixelfedApi,
    private val json: Json
) {
    fun getConversations() = loadListResources {
        api.getConversations()
    }

    fun getChat(accountId: String, maxId: String = "") = loadResource {
        if (maxId.isEmpty()) {
            api.getChat(accountId)
        } else {
            api.getChat(accountId, maxId)
        }
    }

    fun sendMessage(createMessageDto: CreateMessageDto) = loadResource {
        api.sendMessage(json.encodeToString(createMessageDto))
    }

    fun deleteMessage(id: String) = loadType {
        api.deleteMessage(id)
    }
}