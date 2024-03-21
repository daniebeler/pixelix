package com.daniebeler.pfpixelix.ui.composables.post

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.usecase.SearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CreateCommentViewModel @Inject constructor(
    private val searchUseCase: SearchUseCase
): ViewModel() {
    var replyText by mutableStateOf(TextFieldValue(""))
    var mentionsDropdownOpen by mutableStateOf(false)
    var mentionSuggestions by mutableStateOf(MentionSuggestionsState())
    fun changeText(newText: TextFieldValue) {
        replyText = newText
        val regex = Regex("\\B@\\w+\$")

        val result = regex.find(newText.text)
        if (result == null) {
            mentionsDropdownOpen = false
            return
        }
        mentionsDropdownOpen = result.range.first != result.range.last
        searchMentions(result.value)
    }

    private fun searchMentions(text: String?) {
        if (text == null) {
            return
        }
        val searchUsername = text.substring(1)
        searchUseCase(searchUsername, "accounts").onEach {result ->
            mentionSuggestions = when(result) {
                is Resource.Success -> {
                    if (result.data != null) {
                        MentionSuggestionsState(mentions = result.data.accounts)

                    } else {
                        MentionSuggestionsState(error = result.message ?: "An unexpected error occurred")
                    }
                }

                is Resource.Error -> {
                    MentionSuggestionsState(error = result.message ?: "An unexpected error occurred")
                }

                is Resource.Loading -> {
                    MentionSuggestionsState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun clickMention(acct: String) {
        val index = replyText.text.lastIndexOf("@") + 1
        val newText = replyText.text.substring(0,index) + acct
        replyText = replyText.copy(text = newText, selection = TextRange(newText.length))

    }

}