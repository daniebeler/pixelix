package com.daniebeler.pfpixelix.ui.composables.mention

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.usecase.GetPostUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetRepliesUseCase
import com.daniebeler.pfpixelix.ui.composables.single_post.SinglePostState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MentionViewModel @Inject constructor(
    private val getPostUseCase: GetPostUseCase,
    private val getRepliesUseCase: GetRepliesUseCase
) : ViewModel() {

    var postState by mutableStateOf(SinglePostState())
    var postContextState by mutableStateOf(PostContextState())

    fun loadData(postId: String) {
        getPost(postId)
        getPostContext(postId)
    }

    private fun getPost(postId: String) {
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

    private fun getPostContext(postId: String) {
        getRepliesUseCase(postId).onEach { result ->
            postContextState = when (result) {
                is Resource.Success -> {
                    PostContextState(postContext = result.data)
                }

                is Resource.Error -> {
                    PostContextState(error = result.message ?: "An unexpected error occurred")
                }

                is Resource.Loading -> {
                    PostContextState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}