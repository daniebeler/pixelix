package com.daniebeler.pixels.ui.composables

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.daniebeler.pixels.api.CountryRepository
import com.daniebeler.pixels.domain.model.Post
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

    fun convertTime(createdAt: String) {
        timeAgoString = TimeAgo().covertTimeToText(createdAt) ?: ""
    }

    fun loadReplies(accountId: String, postId: String) {
        CoroutineScope(Dispatchers.Default).launch {
            replies = repository.getReplies(accountId, postId)
        }
    }

}