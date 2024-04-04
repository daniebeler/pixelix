package com.daniebeler.pfpixelix.ui.composables.direct_messages.conversations

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
class ConversationsViewModel @Inject constructor(
    private val getConversationsUseCase: GetConversationsUseCase
) : ViewModel() {

    var conversationsState by mutableStateOf(ConversationsState())

    init {
        getConversationsFirstLoad(false)
    }

    private fun getConversationsFirstLoad(refreshing: Boolean) {
        getConversationsUseCase().onEach { result ->
            conversationsState = when (result) {
                is Resource.Success -> {
                    val endReached = (result.data?.size ?: 0) == 0
                    ConversationsState(
                        conversations = result.data ?: emptyList(), endReached = endReached
                    )
                }

                is Resource.Error -> {
                    ConversationsState(error = result.message ?: "An unexpected error occurred")
                }

                is Resource.Loading -> {
                    ConversationsState(
                        isLoading = true,
                        isRefreshing = refreshing,
                        conversations = conversationsState.conversations
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    fun refresh() {
        getConversationsFirstLoad(true)
    }
}