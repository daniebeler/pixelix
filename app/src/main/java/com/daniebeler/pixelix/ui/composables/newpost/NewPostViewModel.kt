package com.daniebeler.pixelix.ui.composables.newpost

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.daniebeler.pixelix.common.Resource
import com.daniebeler.pixelix.data.remote.dto.CreatePostDto
import com.daniebeler.pixelix.domain.model.Instance
import com.daniebeler.pixelix.domain.repository.CountryRepository
import com.daniebeler.pixelix.utils.GetFile
import com.daniebeler.pixelix.utils.MimeType
import com.daniebeler.pixelix.utils.Navigate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.io.File
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
    lateinit var instance: Instance
    var addImageError by mutableStateOf(Pair("", ""))

    fun updateAltText(index: Int, newAltText: String) {
        images = images.toMutableList().also {
            it[index] = it[index].copy(text = newAltText)
        }
    }

    init {
        getInstance()
    }

    private fun bytesIntoHumanReadable(bytes: Long): String? {
        val kilobyte: Long = 1024
        val megabyte = kilobyte * 1024
        val gigabyte = megabyte * 1024
        val terabyte = gigabyte * 1024
        return if (bytes >= 0 && bytes < kilobyte) {
            "$bytes B"
        } else if (bytes >= kilobyte && bytes < megabyte) {
            (bytes / kilobyte).toString() + " KB"
        } else if (bytes >= megabyte && bytes < gigabyte) {
            (bytes / megabyte).toString() + " MB"
        } else if (bytes >= gigabyte && bytes < terabyte) {
            (bytes / gigabyte).toString() + " GB"
        } else if (bytes >= terabyte) {
            (bytes / terabyte).toString() + " TB"
        } else {
            "$bytes Bytes"
        }
    }

    fun addImage(uri: Uri, context: Context) {
        val fileType = MimeType().getMimeType(uri, context.contentResolver) ?: "image/*"
        if (!instance.configuration.mediaAttachmentConfig.supportedMimeTypes.contains(fileType)) {
            addImageError = Pair(
                "Media type is not supported",
                "The media type $fileType is not supportet by this server"
            )
            return
        }
        val file = GetFile().getFile(uri, context) ?: return

        val size = file.length()
        if (fileType.take(5) == "image") {
            if (size > instance.configuration.mediaAttachmentConfig.imageSizeLimit) {
                addImageError = Pair(
                    "Image is to big",
                    "This image is to big, the max size for this server is ${
                        bytesIntoHumanReadable(
                            instance.configuration.mediaAttachmentConfig.imageSizeLimit.toLong()
                        )
                    }, your video has ${bytesIntoHumanReadable(size)}"
                )
                return
            }
        } else if (fileType.take(5) == "video") {
            if (size > instance.configuration.mediaAttachmentConfig.videoSizeLimit) {
                addImageError = Pair(
                    "Video is to big",
                    "This Video is to big, the max size for this server is ${
                        bytesIntoHumanReadable(
                            instance.configuration.mediaAttachmentConfig.videoSizeLimit.toLong()
                        )
                    }, your video has ${bytesIntoHumanReadable(size)}"
                )
                return
            }
        }
        images += ImageItem(uri, "")
    }

    fun post(context: Context, navController: NavController) {
        uploadImage(context, navController, instance)
    }

    private fun getInstance() {
        repository.getInstance().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    if (result.data != null) {
                        instance = result.data
                    } else {
                        createPostState = CreatePostState(
                            error = result.message ?: "An unexpected error occurred"
                        )
                    }
                }

                is Resource.Error -> {
                }

                is Resource.Loading -> {
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun uploadImage(context: Context, navController: NavController, instance: Instance) {
        createPostState = CreatePostState(isLoading = true)
        images.map { image ->
            repository.uploadMedia(
                image.imageUri,
                image.text,
                context,
                instance.configuration.mediaAttachmentConfig
            ).onEach { result ->
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
                        if (!result.message.isNullOrEmpty()) {
                            MediaUploadState(error = result.message)
                        } else {
                            MediaUploadState(error = "An unexpected error occured")
                        }
                    }

                    is Resource.Loading -> {
                        if (mediaUploadState.error != "") {
                            mediaUploadState
                        } else {
                            mediaUploadState.copy(isLoading = true)
                        }
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