package com.daniebeler.pfpixelix.ui.composables.direct_messages.chat

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.usecase.GetChatUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetConversationsUseCase
import com.daniebeler.pfpixelix.ui.composables.direct_messages.conversations.ConversationsState
import com.daniebeler.pfpixelix.ui.composables.notifications.NotificationsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val getChatUseCase: GetChatUseCase
) : ViewModel() {

    var chatState by mutableStateOf(ChatState())

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
                        isLoading = true,
                        chat = chatState.chat
                    )
                }
            }
        }.launchIn(viewModelScope)
    }
}