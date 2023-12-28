package com.daniebeler.pixels.ui.composables

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.daniebeler.pixels.domain.repository.CountryRepository
import com.daniebeler.pixels.domain.model.Reply
import com.daniebeler.pixels.utils.TimeAgo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val repository: CountryRepository
): ViewModel() {

    var replies: List<Reply> by mutableStateOf(emptyList())

    var timeAgoString: String by mutableStateOf("")

    var showPost: Boolean by mutableStateOf(false)

    fun toggleShowPost() {
        showPost = !showPost
    }

    fun convertTime(createdAt: String) {
        timeAgoString = TimeAgo().covertTimeToText(createdAt) ?: ""
    }

    fun loadReplies(accountId: String, postId: String) {
        CoroutineScope(Dispatchers.Default).launch {
            replies = repository.getReplies(accountId, postId)
        }
    }

    fun likePost(postId: String) {
        CoroutineScope(Dispatchers.Default).launch {
            var res = repository.likePost(postId)
            if (res != null) {

            }
        }
    }

    fun unlikePost(postId: String) {
        CoroutineScope(Dispatchers.Default).launch {
            var res = repository.unlikePost(postId)
            if (res != null) {

            }
        }
    }

}