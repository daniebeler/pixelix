package com.daniebeler.pfpixelix.ui.composables.direct_messages.chat

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.usecase.GetConversationsUseCase
import com.daniebeler.pfpixelix.ui.composables.notifications.NotificationsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
) : ViewModel() {

    var chatState by mutableStateOf(ChatState())

    init {
        getChat()
    }

    private fun getChat() {
        chatState = ChatState()
    }
}