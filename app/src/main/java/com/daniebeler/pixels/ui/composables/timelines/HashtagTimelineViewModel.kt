package com.daniebeler.pixels.ui.composables.timelines

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.daniebeler.pixels.domain.repository.CountryRepository
import com.daniebeler.pixels.domain.model.Post
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HashtagTimelineViewModel @Inject constructor(
    private val repository: CountryRepository
): ViewModel() {

    var posts: List<Post> by mutableStateOf(emptyList())

    fun loadPosts(hashtag: String) {
        CoroutineScope(Dispatchers.Default).launch {
            posts = repository.getHashtagTimeline(hashtag)
        }
    }
}