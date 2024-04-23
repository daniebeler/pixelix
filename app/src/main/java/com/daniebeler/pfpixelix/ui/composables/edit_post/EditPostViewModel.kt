package com.daniebeler.pfpixelix.ui.composables.edit_post

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.data.remote.dto.UpdatePostDto
import com.daniebeler.pfpixelix.domain.model.MediaAttachment
import com.daniebeler.pfpixelix.domain.usecase.GetPostUseCase
import com.daniebeler.pfpixelix.domain.usecase.UpdatePostUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class EditPostViewModel @Inject constructor(
    private val getPostUseCase: GetPostUseCase, private val updatePostUseCase: UpdatePostUseCase
) : ViewModel() {
    var editPostState by mutableStateOf(EditPostState())
    var caption by mutableStateOf(TextFieldValue())
    var sensitive: Boolean by mutableStateOf(false)
    var sensitiveText: String by mutableStateOf("")
    var mediaAttachments = mutableStateListOf<MediaAttachment>()

    fun loadData(postId: String) {
        loadPost(postId)
    }

    private fun loadPost(postId: String) {
        getPostUseCase(postId).onEach { result ->
            editPostState = when (result) {
                is Resource.Success -> {
                    if (result.data != null) {
                        caption = TextFieldValue(result.data.content)
                        sensitive = result.data.sensitive
                        sensitiveText = result.data.spoilerText
                        mediaAttachments.addAll(result.data.mediaAttachments)
                    }
                    EditPostState(post = result.data)
                }

                is Resource.Error -> {
                    EditPostState(error = result.message ?: "An unexpected error occurred")
                }

                is Resource.Loading -> {
                    EditPostState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun updatePost(postId: String) {
        val updatePostDto = UpdatePostDto(_status = caption.text,
            _sensitive = sensitive,
            _spoilerText = sensitiveText,
            _media_ids = mediaAttachments.map { it.id })
        updatePostUseCase(
            postId, updatePostDto
        ).onEach { result ->
            editPostState = when (result) {
                is Resource.Success -> {
                    EditPostState(post = editPostState.post)
                }

                is Resource.Error -> {
                    EditPostState(error = result.message ?: "An unexpected error occurred")
                }

                is Resource.Loading -> {
                    EditPostState(isLoading = true, post = editPostState.post)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun moveMediaAttachmentUp(index: Int) {
        if (index >= 1) {
            val copy = mediaAttachments[index]
            mediaAttachments[index] = mediaAttachments[index - 1]
            mediaAttachments[index - 1] = copy
        }
    }

    fun moveMediaAttachmentDown(index: Int) {
        if (index < mediaAttachments.size - 1) {
            val copy = mediaAttachments[index]
            mediaAttachments[index] = mediaAttachments[index + 1]
            mediaAttachments[index + 1] = copy
        }
    }
}

