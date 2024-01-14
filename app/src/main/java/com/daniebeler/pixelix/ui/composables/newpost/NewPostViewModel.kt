package com.daniebeler.pixelix.ui.composables.newpost

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniebeler.pixelix.common.Resource
import com.daniebeler.pixelix.domain.repository.CountryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class NewPostViewModel @Inject constructor(
    private val repository: CountryRepository
) : ViewModel() {
    var uris: List<Uri> = emptyList()
    var caption: String by mutableStateOf("")
    var altText: String by mutableStateOf("")
    var sensitive: Boolean by mutableStateOf(false)
    var sensitiveText: String by mutableStateOf("")
    var audience: String by mutableStateOf("public")
    var mediaUploadState by mutableStateOf(MediaUploadState())
    fun post(context: Context) {
        uris.forEach{
            repository.uploadMedia(it, context).onEach {result ->
                mediaUploadState = when (result) {
                    is Resource.Success -> {
                        println(result.data)
                        MediaUploadState(mediaAttachment = result.data)
                    }

                    is Resource.Error -> {
                        MediaUploadState(error = result.message ?: "An unexpected error occurred")
                    }

                    is Resource.Loading -> {
                        MediaUploadState(isLoading = true)
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    private fun createNewPost() {

    }
}