package com.daniebeler.pfpixelix.ui.composables.direct_messages.conversations

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.model.Account
import com.daniebeler.pfpixelix.domain.usecase.GetConversationsUseCase
import com.daniebeler.pfpixelix.domain.usecase.SearchUseCase
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import me.tatarka.inject.annotations.Inject

class ConversationsViewModel @Inject constructor(
    private val getConversationsUseCase: GetConversationsUseCase,
    private val searchUseCase: SearchUseCase
) : ViewModel() {

    var conversationsState by mutableStateOf(ConversationsState())
    var newConversationUsername by mutableStateOf(TextFieldValue())
    var newConversationState by mutableStateOf(NewConversationState())
    var newConversationSelectedAccount by mutableStateOf<Account?>(null)
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

    fun changeNewConversationUsername(newUsername: TextFieldValue) {
        newConversationSelectedAccount = null
        newConversationUsername = newUsername
        searchUseCase(newUsername.text, "accounts").onEach { result ->
            newConversationState = when (result) {
                is Resource.Success -> {
                    if (result.data != null) {
                        NewConversationState(suggestions = result.data.accounts)
                    } else {
                        NewConversationState(
                            error = result.message ?: "An unexpected error occurred"
                        )
                    }
                }

                is Resource.Error -> {
                    NewConversationState(error = result.message ?: "An unexpected error occurred")
                }

                is Resource.Loading -> {
                    NewConversationState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun refresh() {
        getConversationsFirstLoad(true)
    }
}