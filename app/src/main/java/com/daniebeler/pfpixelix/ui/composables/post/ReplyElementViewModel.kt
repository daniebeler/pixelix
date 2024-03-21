package com.daniebeler.pfpixelix.ui.composables.post

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.model.Reply
import com.daniebeler.pfpixelix.domain.usecase.CreateReplyUseCase
import com.daniebeler.pfpixelix.domain.usecase.DeletePostUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetRepliesUseCase
import com.daniebeler.pfpixelix.domain.usecase.LikePostUseCase
import com.daniebeler.pfpixelix.domain.usecase.UnlikePostUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ReplyElementViewModel @Inject constructor(
    private val getRepliesUseCase: GetRepliesUseCase,
    private val createReplyUseCase: CreateReplyUseCase,
    private val deletePostUseCase: DeletePostUseCase,
    private val likePostUseCase: LikePostUseCase,
    private val unlikePostUseCase: UnlikePostUseCase
    ): ViewModel()
{
    var repliesState by mutableStateOf(RepliesState())
    var likedReply by mutableStateOf(false)

    fun onInit(reply: Reply, myAccountId: String) {
        likedReply = reply.likedBy?.id == myAccountId
    }

    fun loadReplies(accountId: String, replyId: String) {
        getRepliesUseCase(accountId, replyId).onEach { result ->
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

    fun createReply(replyId: String, replyText: String, myAccountId: String) {
        if (replyText.isNotBlank()) {
            createReplyUseCase(replyId, replyText).onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        loadReplies(myAccountId, replyId)
                    }

                    is Resource.Error -> {
                        //ownReplyState =
                          //  OwnReplyState(error = result.message ?: "An unexpected error occurred")
                    }

                    is Resource.Loading -> {
                        //ownReplyState = OwnReplyState(isLoading = true)
                    }
                }
            }.launchIn(viewModelScope)
        }

    }

    fun deleteReply(postId: String) {
        deletePostUseCase(postId).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    repliesState = repliesState.copy(replies = repliesState.replies.filter { it.id != postId })
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

    fun likeReply(postId: String) {
        likePostUseCase(postId).onEach {result ->
            when (result) {
                is Resource.Success -> {
                    likedReply = true
                }

                is Resource.Error -> {
                    likedReply = false
                }

                is Resource.Loading -> {
                    println("is loading")
                }
            }
        }.launchIn(viewModelScope)
    }

    fun unlikeReply(postId: String) {
        unlikePostUseCase(postId).onEach {result ->
            when (result) {
                is Resource.Success -> {
                    likedReply = false
                }

                is Resource.Error -> {
                    likedReply = true
                }

                is Resource.Loading -> {
                    println("is loading")
                }
            }
        }.launchIn(viewModelScope)
    }
}