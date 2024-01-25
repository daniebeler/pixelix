package com.daniebeler.pixelix.ui.composables.newpost

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.daniebeler.pixelix.common.Resource
import com.daniebeler.pixelix.data.remote.dto.CreatePostDto
import com.daniebeler.pixelix.domain.repository.CountryRepository
import com.daniebeler.pixelix.ui.composables.post.LikeState
import com.daniebeler.pixelix.ui.composables.post.RepliesState
import com.daniebeler.pixelix.utils.Navigate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewPostViewModel @Inject constructor(
    private val repository: CountryRepository
) : ViewModel() {
    data class ImageItem(val imageUri: Uri, var text: String = "")
    var images by mutableStateOf(emptyList<ImageItem>())
    var caption: String by mutableStateOf("")
    var sensitive: Boolean by mutableStateOf(false)
    var sensitiveText: String by mutableStateOf("")
    var audience: String by mutableStateOf("public")
    var mediaUploadState by mutableStateOf(MediaUploadState())
    var createPostState by mutableStateOf(CreatePostState())

    fun updateAltText(index: Int, newAltText: String) {
        images = images.toMutableList().also {
            it[index] = it[index].copy(text = newAltText)
        }
    }

    fun post(context: Context, navController: NavController) {
        createPostState = CreatePostState(isLoading = true)
        images.map { image ->
            repository.uploadMedia(image.imageUri, image.text, context).onEach { result ->
                mediaUploadState = when (result) {
                    is Resource.Success -> {
                        val newMediaUploadState = mediaUploadState.copy(
                            mediaAttachments = mediaUploadState.mediaAttachments + result.data!!
                        )
                        println(result.data)
                        if (images.size == newMediaUploadState.mediaAttachments.size) {
                            createNewPost(newMediaUploadState, navController)
                        }
                        newMediaUploadState
                    }

                    is Resource.Error -> {
                        mediaUploadState.copy(error = "An unexpected error occured")
                    }

                    is Resource.Loading -> {
                        mediaUploadState.copy(isLoading = true)
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    private fun createNewPost(newMediaUploadState: MediaUploadState, navController: NavController) {
        val mediaIds = newMediaUploadState.mediaAttachments.map { it.id }
        val createPostDto = CreatePostDto(caption, mediaIds, sensitive, audience, sensitiveText)
        repository.createPost(createPostDto).onEach { result ->
            createPostState = when (result) {
                is Resource.Success -> {
                    if (result.data != null) {
                        Navigate().navigateAndDeleteBackStack("own_profile_screen", navController)
                    }
                    CreatePostState(post = result.data)
                }

                is Resource.Error -> {
                    CreatePostState(error = result.message ?: "An unexpected error occurred")
                }

                is Resource.Loading -> {
                    CreatePostState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}