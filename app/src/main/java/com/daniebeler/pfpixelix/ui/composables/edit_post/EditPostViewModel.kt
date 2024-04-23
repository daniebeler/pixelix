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
import com.daniebeler.pfpixelix.domain.usecase.UpdateMediaUseCase
import com.daniebeler.pfpixelix.domain.usecase.UpdatePostUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditPostViewModel @Inject constructor(
    private val getPostUseCase: GetPostUseCase,
    private val updatePostUseCase: UpdatePostUseCase,
    private val updateMediaUseCase: UpdateMediaUseCase
) : ViewModel() {
    data class MediaDescriptionItem(
        val imageId: String, var description: String, var changed: Boolean
    )

    var editPostState by mutableStateOf(EditPostState())
    var caption by mutableStateOf(TextFieldValue())
    var sensitive: Boolean by mutableStateOf(false)
    var sensitiveText: String by mutableStateOf("")
    var mediaAttachments = mutableStateListOf<MediaAttachment>()
    var mediaDescriptionItems = mutableStateListOf<MediaDescriptionItem>()
    var deleteMediaDialog by mutableStateOf<String?>(null)

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
                        mediaDescriptionItems.addAll(result.data.mediaAttachments.map {
                            MediaDescriptionItem(
                                it.id, it.description ?: "", false
                            )
                        })
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
        CoroutineScope(Dispatchers.Default).launch {

            mediaDescriptionItems.onEach { mediaDescriptionItem ->
                updateMedia(mediaDescriptionItem)
            }

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
    }

    private fun updateMedia(mediaDescriptionItem: MediaDescriptionItem) {
        updateMediaUseCase(
            mediaDescriptionItem.imageId, mediaDescriptionItem.description
        ).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    println("success")
                }

                is Resource.Error -> {
                    editPostState =
                        EditPostState(error = (result.message ?: "An unexpected error occurred"))
                }

                is Resource.Loading -> {
                    editPostState = EditPostState(isLoading = true, post = editPostState.post)

                }
            }
        }.launchIn(viewModelScope)
    }

    fun deleteMedia(mediaId: String) {
        mediaAttachments.remove(mediaAttachments.find { it.id == mediaId })
        mediaDescriptionItems.remove(mediaDescriptionItems.find { it.imageId == mediaId })
        deleteMediaDialog = null
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

