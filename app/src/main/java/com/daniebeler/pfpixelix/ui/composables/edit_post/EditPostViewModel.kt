package com.daniebeler.pfpixelix.ui.composables.edit_post

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.usecase.GetPostUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class EditPostViewModel @Inject constructor(
    private val getPostUseCase: GetPostUseCase
): ViewModel() {
    var editPostState by mutableStateOf(EditPostState())

    fun loadData(postId: String) {
        loadPost(postId)
    }

    private fun loadPost(postId: String) {
        getPostUseCase(postId).onEach { result ->
            editPostState = when (result) {
                is Resource.Success -> {
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

}