package com.daniebeler.pfpixelix.ui.composables.direct_messages.chat

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.data.remote.dto.CreateMessageDto
import com.daniebeler.pfpixelix.domain.model.Message
import com.daniebeler.pfpixelix.domain.usecase.DeleteMessageUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetChatUseCase
import com.daniebeler.pfpixelix.domain.usecase.SendMessageUseCase
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import me.tatarka.inject.annotations.Inject

class ChatViewModel @Inject constructor(
    private val getChatUseCase: GetChatUseCase, private val sendMessageUseCase: SendMessageUseCase, private val deleteMessageUseCase: DeleteMessageUseCase
) : ViewModel() {

    var chatState by mutableStateOf(ChatState())
    var newMessage by mutableStateOf("")
    var newMessageState by mutableStateOf(NewMessageState())
    fun getChat(accountId: String, refreshing: Boolean = false) {
        getChatUseCase(accountId).onEach { result ->
            chatState = when (result) {
                is Resource.Success -> {
                    ChatState(
                        chat = result.data,
                    )
                }

                is Resource.Error -> {
                    ChatState(error = result.message ?: "An unexpected error occurred")
                }

                is Resource.Loading -> {
                    ChatState(
                        isLoading = true, chat = chatState.chat, isRefreshing = refreshing
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getChatPaginated(accountId: String) {
        if (chatState.chat != null && !chatState.isLoading && !chatState.endReached) {
            if (chatState.chat!!.messages.isNotEmpty()) {
                getChatUseCase(accountId, chatState.chat!!.messages.last().id).onEach { result ->
                    chatState = when (result) {
                        is Resource.Success -> {
                            val endReached = result.data?.messages!!.isEmpty()

                            val existingMessageIds = chatState.chat?.messages?.map { it.id }?.toSet() ?: emptySet()
                            val newMessages = result.data.messages.filter { it.id !in existingMessageIds }
                            val messages = (chatState.chat?.messages ?: emptyList()) + newMessages

                            val chat = chatState.chat?.copy()
                            if (chat != null) {
                                chat.messages = messages
                                ChatState(
                                    chat = chat,
                                    endReached = endReached
                                )
                            } else {
                                ChatState(
                                    chat = result.data
                                )
                            }
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
        }
    }

    fun sendMessage(accountId: String) {
        val createMessageDto = CreateMessageDto(
            to_id = accountId, message = newMessage, type = "text"
        )
        newMessage = ""
        sendMessageUseCase(createMessageDto).onEach { result ->
            newMessageState = when (result) {
                is Resource.Success -> {
                    if (result.data != null) {
                        val messages = emptyList<Message>() + result.data + chatState.chat!!.messages
                        val chat = chatState.chat?.copy()
                        if (chat != null) {
                            chat.messages = messages
                            chatState = ChatState(chat = chat)
                        }
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

    fun deleteMessage(id: String) {
        deleteMessageUseCase(id).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    if (result.data != null) {
                        val messages = chatState.chat!!.messages.filter { it.reportId != id }
                        val chat = chatState.chat?.copy()
                        if (chat != null) {
                            chat.messages = messages
                            chatState = ChatState(chat = chat)
                        }
                    }
                }

                is Resource.Error -> {
                    println(result.message)
                }

                is Resource.Loading -> {
                    println("is loading")
                }
            }
        }.launchIn(viewModelScope)
    }
}