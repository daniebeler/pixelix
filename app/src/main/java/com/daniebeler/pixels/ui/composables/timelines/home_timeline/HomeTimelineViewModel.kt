package com.daniebeler.pixels.ui.composables.timelines.home_timeline

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
class HomeTimelineViewModel @Inject constructor(
    private val repository: CountryRepository
): ViewModel() {

    var homeTimeline: List<Post> by mutableStateOf(emptyList())

    init {
        CoroutineScope(Dispatchers.Default).launch {
            homeTimeline = repository.getHomeTimeline()
        }
    }

    fun loadMorePosts() {
        CoroutineScope(Dispatchers.Default).launch {
            homeTimeline += repository.getHomeTimeline(homeTimeline.last().id)
        }
    }
}