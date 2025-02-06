package com.daniebeler.pfpixelix.ui.composables.textfield_mentions

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.getTextBeforeSelection
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.usecase.SearchUseCase
import com.daniebeler.pfpixelix.ui.composables.post.MentionSuggestionsState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import me.tatarka.inject.annotations.Inject

class TextFieldMentionsViewModel @Inject constructor(
    private val searchUseCase: SearchUseCase
) : ViewModel() {
    var text by mutableStateOf(TextFieldValue(""))
    var mentionsDropdownOpen by mutableStateOf(false)
    var mentionSuggestions by mutableStateOf(MentionSuggestionsState())

    fun changeText(newText: TextFieldValue) {
        text = newText
        val regex = Regex("\\B@\\w+\$")

        val textBeforeSelection = newText.getTextBeforeSelection(9999).toString()
        val result = regex.find(textBeforeSelection)

        if (result == null) {
            mentionsDropdownOpen = false
            return
        }
        mentionsDropdownOpen = result.range.first != result.range.last
        searchMentions(result.value)
    }

    private fun searchMentions(mention: String?) {
        if (mention == null) {
            return
        }
        val searchUsername = mention.substring(1)
        searchUseCase(searchUsername, "accounts").onEach { result ->
            mentionSuggestions = when (result) {
                is Resource.Success -> {
                    if (result.data != null) {
                        MentionSuggestionsState(mentions = result.data.accounts)

                    } else {
                        MentionSuggestionsState(
                            error = result.message ?: "An unexpected error occurred"
                        )
                    }
                }

                is Resource.Error -> {
                    MentionSuggestionsState(
                        error = result.message ?: "An unexpected error occurred"
                    )
                }

                is Resource.Loading -> {
                    MentionSuggestionsState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun clickMention(acct: String) {
        val index = text.text.lastIndexOf("@") + 1
        val newText = text.text.substring(0, index) + acct
        text = text.copy(text = newText, selection = TextRange(newText.length))

    }
}