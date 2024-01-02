package com.daniebeler.pixelix.ui.composables.post

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniebeler.pixelix.common.Resource
import com.daniebeler.pixelix.domain.repository.CountryRepository
import com.daniebeler.pixelix.domain.model.Reply
import com.daniebeler.pixelix.ui.composables.notifications.NotificationsState
import com.daniebeler.pixelix.utils.TimeAgo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val repository: CountryRepository
): ViewModel() {

    var repliesState by mutableStateOf(RepliesState())

    var likeState by mutableStateOf(LikeState())
    var bookmarkState by mutableStateOf(BookmarkState())

    var timeAgoString: String by mutableStateOf("")

    var showPost: Boolean by mutableStateOf(false)

    fun toggleShowPost() {
        showPost = !showPost
    }

    fun convertTime(createdAt: String) {
        timeAgoString = TimeAgo().covertTimeToText(createdAt) ?: ""
    }

    fun loadReplies(accountId: String, postId: String) {
        repository.getReplies(accountId, postId).onEach { result ->
            repliesState = when (result) {
                is Resource.Success -> {
                    RepliesState(replies = result.data ?: emptyList())
                }

                is Resource.Error -> {
                    RepliesState(error = result.message ?: "An unexpected error occurred")
                }

                is Resource.Loading -> {
                    RepliesState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun likePost(postId: String) {
        CoroutineScope(Dispatchers.Default).launch {
            repository.likePost(postId).onEach {result ->
                likeState = when (result) {
                    is Resource.Success -> {
                        LikeState(liked = result.data?.favourited ?: false)
                    }

                    is Resource.Error -> {
                        LikeState(error = result.message ?: "An unexpected error occurred")
                    }

                    is Resource.Loading -> {
                        LikeState(isLoading = true)
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    fun unlikePost(postId: String) {
        CoroutineScope(Dispatchers.Default).launch {
            repository.unlikePost(postId).onEach {result ->
                likeState = when (result) {
                    is Resource.Success -> {
                        LikeState(liked = result.data?.favourited ?: false)
                    }

                    is Resource.Error -> {
                        LikeState(error = result.message ?: "An unexpected error occurred")
                    }

                    is Resource.Loading -> {
                        LikeState(isLoading = true)
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    fun bookmarkPost(postId: String) {
        CoroutineScope(Dispatchers.Default).launch {
            repository.bookmarkPost(postId).onEach {result ->
                bookmarkState = when (result) {
                    is Resource.Success -> {
                        BookmarkState(bookmarked = result.data?.bookmarked ?: false)
                    }

                    is Resource.Error -> {
                        BookmarkState(error = result.message ?: "An unexpected error occurred")
                    }

                    is Resource.Loading -> {
                        BookmarkState(isLoading = true)
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    fun unbookmarkPost(postId: String) {
        CoroutineScope(Dispatchers.Default).launch {
            repository.unbookmarkPost(postId).onEach {result ->
                bookmarkState = when (result) {
                    is Resource.Success -> {
                        BookmarkState(bookmarked = result.data?.bookmarked ?: false)
                    }

                    is Resource.Error -> {
                        BookmarkState(error = result.message ?: "An unexpected error occurred")
                    }

                    is Resource.Loading -> {
                        BookmarkState(isLoading = true)
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

}