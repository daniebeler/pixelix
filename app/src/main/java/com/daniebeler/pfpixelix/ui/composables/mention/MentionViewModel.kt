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
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import me.tatarka.inject.annotations.Inject

class MentionViewModel @Inject constructor(
    private val getPostUseCase: GetPostUseCase,
    private val getRepliesUseCase: GetRepliesUseCase
) : ViewModel() {

    var postState by mutableStateOf(SinglePostState())
    var postContextState by mutableStateOf(PostContextState())

    fun loadData(postId: String, refreshing: Boolean = false) {
        getPost(postId, refreshing)
        getPostContext(postId, refreshing)
    }

    private fun getPost(postId: String, refreshing: Boolean) {
        getPostUseCase(postId).onEach { result ->
            postState = when (result) {
                is Resource.Success -> {
                    SinglePostState(post = result.data)
                }

                is Resource.Error -> {
                    SinglePostState(error = result.message ?: "An unexpected error occurred")
                }

                is Resource.Loading -> {
                    SinglePostState(isLoading = true, refreshing)
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getPostContext(postId: String, refreshing: Boolean) {
        getRepliesUseCase(postId).onEach { result ->
            postContextState = when (result) {
                is Resource.Success -> {
                    PostContextState(postContext = result.data)
                }

                is Resource.Error -> {
                    PostContextState(error = result.message ?: "An unexpected error occurred")
                }

                is Resource.Loading -> {
                    PostContextState(isLoading = true, refreshing)
                }
            }
        }.launchIn(viewModelScope)
    }
}