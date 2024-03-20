package com.daniebeler.pfpixelix.ui.composables.post

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.model.Post
import com.daniebeler.pfpixelix.domain.usecase.BookmarkPostUseCase
import com.daniebeler.pfpixelix.domain.usecase.CreateReplyUseCase
import com.daniebeler.pfpixelix.domain.usecase.DeletePostUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetAccountsWhoLikedPostUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetOwnAccountIdUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetRepliesUseCase
import com.daniebeler.pfpixelix.domain.usecase.LikePostUseCase
import com.daniebeler.pfpixelix.domain.usecase.OpenExternalUrlUseCase
import com.daniebeler.pfpixelix.domain.usecase.UnbookmarkPostUseCase
import com.daniebeler.pfpixelix.domain.usecase.UnlikePostUseCase
import com.daniebeler.pfpixelix.utils.TimeAgo
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
    private val getRepliesUseCase: GetRepliesUseCase,
    private val createReplyUseCase: CreateReplyUseCase,
    private val likePostUseCase: LikePostUseCase,
    private val unlikePostUseCase: UnlikePostUseCase,
    private val bookmarkPostUseCase: BookmarkPostUseCase,
    private val unbookmarkPostUseCase: UnbookmarkPostUseCase,
    private val deletePostUseCase: DeletePostUseCase,
    private val getOwnAccountIdUseCase: GetOwnAccountIdUseCase,
    private val getAccountsWhoLikedPostUseCase: GetAccountsWhoLikedPostUseCase,
    private val openExternalUrlUseCase: OpenExternalUrlUseCase
) : ViewModel() {

    var post: Post? by mutableStateOf(null)

    var repliesState by mutableStateOf(RepliesState())

    var ownReplyState by mutableStateOf(OwnReplyState())

    var likedByState by mutableStateOf(LikedByState())

    var deleteState by mutableStateOf(DeleteState())
    var deleteDialog: String? by mutableStateOf(null)
    var timeAgoString: String by mutableStateOf("")

    var showPost: Boolean by mutableStateOf(false)

    var myAccountId: String? = null

    var newComment: String by mutableStateOf("")

    init {
        CoroutineScope(Dispatchers.Default).launch {
            myAccountId = getOwnAccountIdUseCase().first()
        }
    }

    fun deletePost(postId: String) {
        deleteDialog = null
        deletePostUseCase(postId).onEach { result ->
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
        timeAgoString = TimeAgo.convertTimeToText(createdAt)
    }

    fun loadReplies(accountId: String, postId: String) {
        getRepliesUseCase(accountId, postId).onEach { result ->
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
            createReplyUseCase(postId, newComment).onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        ownReplyState = OwnReplyState(reply = result.data)
                        myAccountId?.let { loadReplies(it, postId) }
                        newComment = ""
                    }

                    is Resource.Error -> {
                        ownReplyState =
                            OwnReplyState(error = result.message ?: "An unexpected error occurred")
                    }

                    is Resource.Loading -> {
                        ownReplyState = OwnReplyState(isLoading = true)
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    fun loadLikedBy(postId: String) {
        getAccountsWhoLikedPostUseCase(postId).onEach { result ->
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
        if (post?.favourited == false) {
            post = post?.copy(favourited = true)
            CoroutineScope(Dispatchers.Default).launch {
                likePostUseCase(postId).onEach { result ->
                    when (result) {
                        is Resource.Success -> {
                            post = post?.copy(favourited = result.data?.favourited ?: false)
                        }

                        is Resource.Error -> {
                            post = post?.copy(favourited = false)
                        }

                        is Resource.Loading -> {
                        }
                    }
                }.launchIn(viewModelScope)
            }
        }

    }

    fun unlikePost(postId: String) {
        if (post?.favourited == true) {
            CoroutineScope(Dispatchers.Default).launch {
                unlikePostUseCase(postId).onEach { result ->
                    when (result) {
                        is Resource.Success -> {
                            post = post?.copy(favourited = result.data?.favourited ?: false)
                        }

                        is Resource.Error -> {
                            post = post?.copy(favourited = true)
                        }

                        is Resource.Loading -> {
                        }
                    }
                }.launchIn(viewModelScope)
            }
        }
    }

    fun bookmarkPost(postId: String) {
        if (post?.bookmarked == false) {
            post = post?.copy(bookmarked = true)
            CoroutineScope(Dispatchers.Default).launch {
                bookmarkPostUseCase(postId).onEach { result ->
                    when (result) {
                        is Resource.Success -> {
                            post = post?.copy(bookmarked = result.data?.bookmarked ?: false)
                        }

                        is Resource.Error -> {
                            post = post?.copy(bookmarked = false)
                        }

                        is Resource.Loading -> {
                        }
                    }
                }.launchIn(viewModelScope)
            }
        }
    }

    fun unBookmarkPost(postId: String) {
        if (post?.bookmarked == true) {
            post = post?.copy(bookmarked = false)
            CoroutineScope(Dispatchers.Default).launch {
                unbookmarkPostUseCase(postId).onEach { result ->
                    when (result) {
                        is Resource.Success -> {
                            post = post?.copy(bookmarked = result.data?.bookmarked ?: false)
                        }

                        is Resource.Error -> {
                            post = post?.copy(bookmarked = true)
                        }

                        is Resource.Loading -> {
                        }
                    }
                }.launchIn(viewModelScope)
            }
        }

    }

    fun openUrl(context: Context, url: String) {
        openExternalUrlUseCase(context, url)
    }
}