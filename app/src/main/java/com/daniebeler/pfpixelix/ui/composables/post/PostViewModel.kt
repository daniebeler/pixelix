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
import com.daniebeler.pfpixelix.domain.usecase.GetCurrentLoginDataUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetRepliesUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetVolumeUseCase
import com.daniebeler.pfpixelix.domain.usecase.LikePostUseCase
import com.daniebeler.pfpixelix.domain.usecase.OpenExternalUrlUseCase
import com.daniebeler.pfpixelix.domain.usecase.ReblogPostUseCase
import com.daniebeler.pfpixelix.domain.usecase.SetVolumeUseCase
import com.daniebeler.pfpixelix.domain.usecase.UnbookmarkPostUseCase
import com.daniebeler.pfpixelix.domain.usecase.UnlikePostUseCase
import com.daniebeler.pfpixelix.domain.usecase.UnreblogPostUseCase
import com.daniebeler.pfpixelix.ui.composables.post.reply.OwnReplyState
import com.daniebeler.pfpixelix.ui.composables.post.reply.RepliesState
import com.daniebeler.pfpixelix.utils.TimeAgo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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
    private val reblogPostUseCase: ReblogPostUseCase,
    private val unreblogPostUseCase: UnreblogPostUseCase,
    private val bookmarkPostUseCase: BookmarkPostUseCase,
    private val unbookmarkPostUseCase: UnbookmarkPostUseCase,
    private val deletePostUseCase: DeletePostUseCase,
    private val currentLoginDataUseCase: GetCurrentLoginDataUseCase,
    private val getAccountsWhoLikedPostUseCase: GetAccountsWhoLikedPostUseCase,
    private val openExternalUrlUseCase: OpenExternalUrlUseCase,
    private val getVolumeUseCase: GetVolumeUseCase,
    private val setVolumeUseCase: SetVolumeUseCase
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

    var volume by mutableStateOf(false)

    init {
        CoroutineScope(Dispatchers.Default).launch {
            myAccountId = currentLoginDataUseCase()!!.accountId
        }
    }

    fun toggleVolume(newVolume: Boolean) {
        volume = newVolume
        viewModelScope.launch {
            setVolumeUseCase(newVolume)
        }
    }

    fun updatePost(post: Post) {
        this.post = post
        getVolume()
    }

    private fun getVolume() {
        viewModelScope.launch {
            getVolumeUseCase().collect { res ->
                volume = res
            }
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

    fun createReply(postId: String, commentText: String) {
        if (commentText.isNotEmpty()) {
            createReplyUseCase(postId, commentText).onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        ownReplyState = OwnReplyState(reply = result.data)
                        loadReplies(post!!.account.id, postId)
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

    fun deleteReply(postId: String) {
        deletePostUseCase(postId).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    repliesState =
                        repliesState.copy(replies = repliesState.replies.filter { it.id != postId })
                }

                is Resource.Error -> {
                    println(result.message)
                }

                is Resource.Loading -> {
                    println("is loading")
                }
            }
        }.launchIn(viewModelScope)
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

    fun reblogPost(postId: String) {
        if (post?.reblogged == false) {
            post = post?.copy(reblogged = true)
            CoroutineScope(Dispatchers.Default).launch {
                reblogPostUseCase(postId).onEach { result ->
                    when (result) {
                        is Resource.Success -> {
                            post = post?.copy(reblogged = result.data?.reblogged ?: false)
                        }

                        is Resource.Error -> {
                            post = post?.copy(reblogged = false)
                        }

                        is Resource.Loading -> {
                        }
                    }
                }.launchIn(viewModelScope)
            }
        }
    }

    fun unreblogPost(postId: String) {
        if (post?.reblogged == true) {
            CoroutineScope(Dispatchers.Default).launch {
                unreblogPostUseCase(postId).onEach { result ->
                    when (result) {
                        is Resource.Success -> {
                            post = post?.copy(reblogged = result.data?.reblogged ?: false)
                        }

                        is Resource.Error -> {
                            post = post?.copy(reblogged = true)
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