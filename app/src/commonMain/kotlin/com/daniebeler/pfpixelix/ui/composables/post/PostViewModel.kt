package com.daniebeler.pfpixelix.ui.composables.post

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.model.LikedBy
import com.daniebeler.pfpixelix.domain.model.Post
import com.daniebeler.pfpixelix.domain.service.account.AccountService
import com.daniebeler.pfpixelix.domain.usecase.BookmarkPostUseCase
import com.daniebeler.pfpixelix.domain.usecase.CreateReplyUseCase
import com.daniebeler.pfpixelix.domain.usecase.DeletePostUseCase
import com.daniebeler.pfpixelix.domain.usecase.DownloadImageUseCase
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
import com.daniebeler.pfpixelix.ui.composables.settings.preferences.prefs.FocusModePrefUtil
import com.daniebeler.pfpixelix.ui.composables.settings.preferences.prefs.HideAltTextButtonPrefUtil
import com.daniebeler.pfpixelix.utils.KmpContext
import com.daniebeler.pfpixelix.utils.TimeAgo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject


class PostViewModel @Inject constructor(
    context: KmpContext,
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
    private val accountService: AccountService,
    private val openExternalUrlUseCase: OpenExternalUrlUseCase,
    private val getVolumeUseCase: GetVolumeUseCase,
    private val setVolumeUseCase: SetVolumeUseCase,
    private val downloadImageUseCase: DownloadImageUseCase
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
    var myUsername: String? = null


    var isAltTextButtonHidden by mutableStateOf(false)
    var isInFocusMode by mutableStateOf(false)

    var volume by mutableStateOf(false)

    init {
        CoroutineScope(Dispatchers.Default).launch {
            myAccountId = currentLoginDataUseCase()!!.accountId
            myUsername = currentLoginDataUseCase()!!.username
        }

        isAltTextButtonHidden = HideAltTextButtonPrefUtil.isEnable(context)

        isInFocusMode = FocusModePrefUtil.isEnable(context)
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

    fun loadReplies(postId: String) {
        getRepliesUseCase(postId).onEach { result ->
            repliesState = when (result) {
                is Resource.Success -> {
                    RepliesState(replies = result.data?.descendants ?: emptyList())
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
                        repliesState = repliesState.copy(replies = repliesState.replies + result.data!!)
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
        accountService.getLikedBy(postId).onEach { result ->
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

    fun likePost(postId: String, updatePost: (Post) -> Unit) {
        if (post?.favourited == false) {
            post = post?.copy(
                favourited = true,
                favouritesCount = post!!.favouritesCount + 1,
                likedBy = post!!.likedBy?.copy(
                    totalCount = post!!.likedBy!!.totalCount + 1,
                    others = true,
                    username = post!!.likedBy!!.username ?: myUsername
                ) ?: LikedBy(
                    totalCount = 1, others = true, username = myUsername, id = myAccountId
                )
            )
            post?.let { updatePost(it) }
            CoroutineScope(Dispatchers.Default).launch {
                likePostUseCase(postId).onEach { result ->
                    when (result) {
                        is Resource.Success -> {
                            post = post?.copy(
                                favourited = result.data?.favourited ?: true,
                                favouritesCount = result.data?.favouritesCount ?: 0,
                            )
                            post?.let { updatePost(it) }
                        }

                        is Resource.Error -> {
                            post = post?.copy(
                                favourited = false,
                                favouritesCount = result.data?.favouritesCount?.minus(1) ?: 0
                            )
                            post?.let { updatePost(it) }
                        }

                        is Resource.Loading -> {
                        }
                    }
                }.launchIn(viewModelScope)
            }
        }
    }

    fun unlikePost(postId: String, updatePost: (Post) -> Unit) {
        if (!post!!.favourited) {
            return
        }
        post = post?.copy(
            favourited = false, favouritesCount = post?.favouritesCount?.minus(
                1
            ) ?: 0
        )
        post?.let { updatePost(it) }

        CoroutineScope(Dispatchers.Default).launch {
            unlikePostUseCase(postId).onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        post = post?.copy(favourited = result.data?.favourited ?: false)
                        result.data?.let { updatePost(result.data) }
                    }

                    is Resource.Error -> {
                        post = post?.copy(
                            favourited = true,
                            favouritesCount = result.data?.favouritesCount?.plus(1) ?: 0
                        )
                        post?.let { updatePost(it) }
                    }

                    is Resource.Loading -> {
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    fun reblogPost(postId: String, updatePost: (Post) -> Unit) {
        if (post?.reblogged == false) {
            post = post?.copy(reblogged = true)
            post?.let { updatePost(it) }
            CoroutineScope(Dispatchers.Default).launch {
                reblogPostUseCase(postId).onEach { result ->
                    when (result) {
                        is Resource.Success -> {
                            post = post?.copy(reblogged = result.data?.reblogged ?: false)
                            result.data?.let { updatePost(result.data) }
                        }

                        is Resource.Error -> {
                            post = post?.copy(reblogged = false)
                            post?.let { updatePost(it) }
                        }

                        is Resource.Loading -> {
                        }
                    }
                }.launchIn(viewModelScope)
            }
        }
    }

    fun unreblogPost(postId: String, updatePost: (Post) -> Unit) {
        if (post?.reblogged == true) {
            post = post?.copy(reblogged = false)
            post?.let { updatePost(it) }
            CoroutineScope(Dispatchers.Default).launch {
                unreblogPostUseCase(postId).onEach { result ->
                    when (result) {
                        is Resource.Success -> {
                            post = post?.copy(reblogged = result.data?.reblogged ?: false)
                            result.data?.let { updatePost(result.data) }
                        }

                        is Resource.Error -> {
                            post = post?.copy(reblogged = true)
                            post?.let { updatePost(it) }
                        }

                        is Resource.Loading -> {
                        }
                    }
                }.launchIn(viewModelScope)
            }
        }
    }

    fun bookmarkPost(postId: String, updatePost: (Post) -> Unit) {
        if (post?.bookmarked == false) {
            post = post?.copy(bookmarked = true)
            post?.let { updatePost(it) }
            CoroutineScope(Dispatchers.Default).launch {
                bookmarkPostUseCase(postId).onEach { result ->
                    when (result) {
                        is Resource.Success -> {
                            post = post?.copy(bookmarked = result.data?.bookmarked ?: false)
                            result.data?.let { updatePost(result.data) }
                        }

                        is Resource.Error -> {
                            post = post?.copy(bookmarked = false)
                            post?.let { updatePost(it) }
                        }

                        is Resource.Loading -> {
                        }
                    }
                }.launchIn(viewModelScope)
            }
        }
    }

    fun unBookmarkPost(postId: String, updatePost: (Post) -> Unit) {
        if (post?.bookmarked == true) {
            post = post?.copy(bookmarked = false)
            post?.let { updatePost(it) }
            CoroutineScope(Dispatchers.Default).launch {
                unbookmarkPostUseCase(postId).onEach { result ->
                    when (result) {
                        is Resource.Success -> {
                            post = post?.copy(bookmarked = result.data?.bookmarked ?: false)
                            result.data?.let { updatePost(result.data) }
                        }

                        is Resource.Error -> {
                            post = post?.copy(bookmarked = true)
                            post?.let { updatePost(it) }
                        }

                        is Resource.Loading -> {
                        }
                    }
                }.launchIn(viewModelScope)
            }
        }

    }

    fun openUrl(url: String, context: KmpContext) {
        openExternalUrlUseCase(url, context)
    }

    fun saveImage(name: String?, url: String, context: KmpContext) {
        downloadImageUseCase(name, url, context)
    }

}