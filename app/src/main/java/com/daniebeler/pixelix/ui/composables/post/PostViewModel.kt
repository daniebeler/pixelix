package com.daniebeler.pixelix.ui.composables.post

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniebeler.pixelix.common.Constants.AUDIENCE_PUBLIC
import com.daniebeler.pixelix.common.Resource
import com.daniebeler.pixelix.domain.repository.CountryRepository
import com.daniebeler.pixelix.utils.TimeAgo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val repository: CountryRepository
) : ViewModel() {

    var repliesState by mutableStateOf(RepliesState())

    var ownReplyState by mutableStateOf(OwnReplyState())

    var likedByState by mutableStateOf(LikedByState())

    var likeState by mutableStateOf(LikeState())
    var bookmarkState by mutableStateOf(BookmarkState())
    var deleteState by mutableStateOf(DeleteState())
    var deleteDialog: String? by mutableStateOf(null)
    var timeAgoString: String by mutableStateOf("")

    var showPost: Boolean by mutableStateOf(false)

    var myAccountId: String? = null

    var newComment: String by mutableStateOf("")

    init {
        CoroutineScope(Dispatchers.Default).launch {
            myAccountId = repository.getAccountId().first()
        }
    }

    fun deletePost(postId: String) {
        deleteDialog = null
        repository.deletePost(postId).onEach { result ->
            deleteState = when (result) {
                is Resource.Success -> {
                    DeleteState(deleted = true)
                }

                is Resource.Error -> {
                    DeleteState(error = result.message ?: "An unexpected error occurred")
                }

                is Resource.Loading -> {
                    DeleteState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

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

    fun createReply(postId: String) {
        if (newComment.isNotEmpty()) {
            repository.createReply(postId, newComment).onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        ownReplyState = OwnReplyState(reply = result.data)
                        newComment = ""
                    }

                    is Resource.Error -> {
                        ownReplyState = OwnReplyState(error = result.message ?: "An unexpected error occurred")
                    }

                    is Resource.Loading -> {
                        ownReplyState = OwnReplyState(isLoading = true)
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    fun loadLikedBy(postId: String) {
        repository.getLikedBy(postId).onEach { result ->
            likedByState = when (result) {
                is Resource.Success -> {
                    LikedByState(likedBy = result.data ?: emptyList())
                }

                is Resource.Error -> {
                    LikedByState(error = result.message ?: "An unexpected error occurred")
                }

                is Resource.Loading -> {
                    LikedByState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun likePost(postId: String) {
        CoroutineScope(Dispatchers.Default).launch {
            repository.likePost(postId).onEach { result ->
                likeState = when (result) {
                    is Resource.Success -> {
                        LikeState(
                            liked = result.data?.favourited ?: false,
                            likesCount = result.data?.favouritesCount ?: likeState.likesCount
                        )
                    }

                    is Resource.Error -> {
                        LikeState(error = result.message ?: "An unexpected error occurred")
                    }

                    is Resource.Loading -> {
                        LikeState(
                            isLoading = true,
                            liked = true,
                            likesCount = if (likeState.liked) {
                                likeState.likesCount
                            } else {
                                likeState.likesCount + 1
                            }
                        )
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    fun unlikePost(postId: String) {
        CoroutineScope(Dispatchers.Default).launch {
            repository.unlikePost(postId).onEach { result ->
                likeState = when (result) {
                    is Resource.Success -> {
                        LikeState(
                            liked = result.data?.favourited ?: false,
                            likesCount = result.data?.favouritesCount ?: likeState.likesCount
                        )
                    }

                    is Resource.Error -> {
                        LikeState(error = result.message ?: "An unexpected error occurred")
                    }

                    is Resource.Loading -> {
                        LikeState(
                            isLoading = true,
                            liked = false,
                            likesCount = if (!likeState.liked) {
                                likeState.likesCount
                            } else {
                                likeState.likesCount - 1
                            }
                        )
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    fun bookmarkPost(postId: String) {
        CoroutineScope(Dispatchers.Default).launch {
            repository.bookmarkPost(postId).onEach { result ->
                bookmarkState = when (result) {
                    is Resource.Success -> {
                        BookmarkState(bookmarked = result.data?.bookmarked ?: false)
                    }

                    is Resource.Error -> {
                        BookmarkState(error = result.message ?: "An unexpected error occurred")
                    }

                    is Resource.Loading -> {
                        BookmarkState(isLoading = true, bookmarked = true)
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    fun unBookmarkPost(postId: String) {
        CoroutineScope(Dispatchers.Default).launch {
            repository.unBookmarkPost(postId).onEach { result ->
                bookmarkState = when (result) {
                    is Resource.Success -> {
                        BookmarkState(bookmarked = result.data?.bookmarked ?: false)
                    }

                    is Resource.Error -> {
                        BookmarkState(error = result.message ?: "An unexpected error occurred")
                    }

                    is Resource.Loading -> {
                        BookmarkState(isLoading = true, bookmarked = false)
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

}