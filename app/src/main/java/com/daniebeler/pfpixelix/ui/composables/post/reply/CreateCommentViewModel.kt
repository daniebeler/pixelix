package com.daniebeler.pfpixelix.ui.composables.post.reply

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.usecase.SearchUseCase
import com.daniebeler.pfpixelix.ui.composables.post.MentionSuggestionsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CreateCommentViewModel @Inject constructor(
    private val searchUseCase: SearchUseCase
): ViewModel() {
    var replyText by mutableStateOf(TextFieldValue(""))
}