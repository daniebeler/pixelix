package com.daniebeler.pfpixelix.ui.composables.newpost

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.daniebeler.pfpixelix.common.Constants.AUDIENCE_PUBLIC
import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.data.remote.dto.CreatePostDto
import com.daniebeler.pfpixelix.domain.model.Instance
import com.daniebeler.pfpixelix.domain.usecase.CreatePostUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetInstanceUseCase
import com.daniebeler.pfpixelix.domain.usecase.UpdateMediaUseCase
import com.daniebeler.pfpixelix.domain.usecase.UploadMediaUseCase
import com.daniebeler.pfpixelix.utils.GetFile
import com.daniebeler.pfpixelix.utils.MimeType
import com.daniebeler.pfpixelix.utils.Navigate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


@HiltViewModel
class NewPostViewModel @Inject constructor(
    private val uploadMediaUseCase: UploadMediaUseCase,
    private val updateMediaUseCase: UpdateMediaUseCase,
    private val createPostUseCase: CreatePostUseCase,
    private val getInstanceUseCase: GetInstanceUseCase
) : ViewModel() {
    data class ImageItem(
        val imageUri: Uri, var id: String?, var text: String = "", var isLoading: Boolean
    )

    var images by mutableStateOf(emptyList<ImageItem>())
    var caption: TextFieldValue by mutableStateOf(TextFieldValue())
    var sensitive: Boolean by mutableStateOf(false)
    var sensitiveText: String by mutableStateOf("")
    var audience: String by mutableStateOf(AUDIENCE_PUBLIC)
    var mediaUploadState by mutableStateOf(MediaUploadState())
    var createPostState by mutableStateOf(CreatePostState())
    lateinit var instance: Instance
    var addImageError by mutableStateOf(Pair("", ""))

    init {
        getInstance()
    }

    private fun getInstance() {
        getInstanceUseCase().onEach { result ->
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

    fun updateAltTextVariable(index: Int, newAltText: String) {
        images = images.toMutableList().also {
            it[index] = it[index].copy(text = newAltText)
        }
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
        val fileType = MimeType.getMimeType(uri, context.contentResolver) ?: "image/*"
        if (!instance.configuration.mediaAttachmentConfig.supportedMimeTypes.contains(fileType)) {
            addImageError = Pair(
                "Media type is not supported",
                "The media type $fileType is not supportet by this server"
            )
            return
        }
        val file = GetFile.getFile(uri, context) ?: return

        val size = file.length()
        if (fileType.take(5) == "image") {
            if (size > instance.configuration.mediaAttachmentConfig.imageSizeLimit) {
                addImageError = Pair(
                    "Image is to big", "This image is to big, the max size for this server is ${
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
                    "Video is to big", "This Video is to big, the max size for this server is ${
                        bytesIntoHumanReadable(
                            instance.configuration.mediaAttachmentConfig.videoSizeLimit.toLong()
                        )
                    }, your video has ${bytesIntoHumanReadable(size)}"
                )
                return
            }
        }
        images += ImageItem(uri, null, "", true)
        uploadImage(context, instance, uri, "")
    }

    private fun uploadImage(context: Context, instance: Instance, uri: Uri, text: String) {
        uploadMediaUseCase(
            uri, text, context, instance.configuration.mediaAttachmentConfig
        ).onEach { result ->
            mediaUploadState = when (result) {
                is Resource.Success -> {
                    if (result.data?.type?.take(5) == "video") {
                        Thread.sleep(1500)
                    }
                    images.find { it.imageUri == uri }?.isLoading = false
                    images.find { it.imageUri == uri }?.id = result.data?.id
                    mediaUploadState.copy(
                        mediaAttachments = mediaUploadState.mediaAttachments + result.data!!,
                        isLoading = false
                    )
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
        }.flowOn(Dispatchers.IO).launchIn(viewModelScope)
    }

    fun post(navController: NavController) {
        if (images.find { it.isLoading } != null && images.isEmpty()) {
            return
        }
        createPostState = CreatePostState(isLoading = true)
        if (images.size == mediaUploadState.mediaAttachments.size) {
            images.forEachIndexed { index, it ->
                if (it.text != "") {
                    updateAltText(index, it.text)
                }
            }
            createNewPost(mediaUploadState, navController)
        }
    }

    private fun updateAltText(index: Int, altText: String) {
        val image = images[index]
        if (image.id == null) {
            return
        }
        updateMediaUseCase(
            image.id!!, image.text
        ).onEach { result ->
            mediaUploadState = when (result) {
                is Resource.Success -> {
                    images.find { it.imageUri == image.imageUri }?.isLoading = false
                    var index: Int = 0
                    mediaUploadState.mediaAttachments.forEachIndexed { i, it ->
                        if (it.id == image.id) {
                            index = i
                        }
                    }
                    mediaUploadState.copy(
                        mediaAttachments = mediaUploadState.mediaAttachments.toMutableList().also {
                            it[index] = result.data!!
                        }, isLoading = false
                    )
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

    private fun createNewPost(newMediaUploadState: MediaUploadState, navController: NavController) {
        val mediaIds = newMediaUploadState.mediaAttachments.map { it.id }
        val createPostDto = CreatePostDto(caption.text, mediaIds, sensitive, audience, sensitiveText)
        createPostUseCase(createPostDto).onEach { result ->
            createPostState = when (result) {
                is Resource.Success -> {
                    if (result.data != null) {
                        Navigate.navigateAndDeleteBackStack("own_profile_screen", navController)
                    }
                    CreatePostState(post = result.data, isLoading = true)
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