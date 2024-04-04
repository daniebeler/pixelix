package com.daniebeler.pfpixelix.ui.composables.direct_messages.chat

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.data.remote.dto.CreateMessageDto
import com.daniebeler.pfpixelix.domain.usecase.GetChatUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetConversationsUseCase
import com.daniebeler.pfpixelix.domain.usecase.SendMessageUseCase
import com.daniebeler.pfpixelix.ui.composables.direct_messages.conversations.ConversationsState
import com.daniebeler.pfpixelix.ui.composables.notifications.NotificationsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val getChatUseCase: GetChatUseCase,
    private val sendMessageUseCase: SendMessageUseCase
) : ViewModel() {

    var chatState by mutableStateOf(ChatState())
    var newMessage by mutableStateOf("")
    var newMessageState by mutableStateOf(NewMessageState())
    fun getChat(accountId: String) {
        getChatUseCase(accountId).onEach { result ->
            chatState = when (result) {
                is Resource.Success -> {
                    ChatState(
                        chat = result.data
                    )
                }

                is Resource.Error -> {
                    ChatState(error = result.message ?: "An unexpected error occurred")
                }

                is Resource.Loading -> {
                    ChatState(
                        isLoading = true, chat = chatState.chat
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    fun sendMessage(accountId: String) {
        val createMessageDto = CreateMessageDto(
            to_id =accountId,
            message = newMessage,
            type = "text"
        )
        sendMessageUseCase(createMessageDto).onEach { result ->
            newMessageState = when (result) {
                is Resource.Success -> {
                    if (result.data != null) {
                        val messages = (chatState.chat?.messages ?: emptyList()) + result.data
                        val chat = chatState.chat?.copy()
                        if (chat != null) {
                            chat.messages = messages
                        }
                        chatState = ChatState(chat = chat)
                    }
                    NewMessageState(message = result.data)
                }

                is Resource.Error -> {
                    NewMessageState(error = result.message ?: "An unexpected error occurred")
                }

                is Resource.Loading -> {
                    NewMessageState(
                        isLoading = true
                    )
                }
            }
        }.launchIn(viewModelScope)

    }
}