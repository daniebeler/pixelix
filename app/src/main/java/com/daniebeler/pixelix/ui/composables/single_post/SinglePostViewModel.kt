package com.daniebeler.pixelix.ui.composables.single_post

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniebeler.pixelix.common.Resource
import com.daniebeler.pixelix.domain.usecase.GetPostUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class SinglePostViewModel @Inject constructor(
    private val getPostUseCase: GetPostUseCase
) : ViewModel() {

    var postState by mutableStateOf(SinglePostState())

    fun getPost(postId: String) {
        getPostUseCase(postId).onEach { result ->
            postState = when (result) {
                is Resource.Success -> {
                    SinglePostState(post = result.data)
                }

                is Resource.Error -> {
                    SinglePostState(error = result.message ?: "An unexpected error occurred")
                }

                is Resource.Loading -> {
                    SinglePostState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}