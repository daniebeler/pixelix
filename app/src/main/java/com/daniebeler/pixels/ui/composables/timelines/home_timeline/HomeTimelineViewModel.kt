package com.daniebeler.pixels.ui.composables.timelines.home_timeline

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniebeler.pixels.common.Resource
import com.daniebeler.pixels.domain.model.Post
import com.daniebeler.pixels.domain.repository.CountryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class HomeTimelineViewModel @Inject constructor(
    private val repository: CountryRepository
): ViewModel() {

    var homeTimelineState by mutableStateOf(HomeTimelineState())

    init {
        loadMorePosts()
    }

    fun loadMorePosts() {

        val maxId = if (homeTimelineState.homeTimeline.isEmpty()) {
            ""
        } else {
            homeTimelineState.homeTimeline.last().id
        }

        repository.getHomeTimeline(maxId).onEach { result ->
            homeTimelineState = when (result) {
                is Resource.Success -> {
                    val newTimeline: List<Post> = homeTimelineState.homeTimeline + (result.data ?: emptyList())
                    HomeTimelineState(homeTimeline = newTimeline)
                }

                is Resource.Error -> {
                    HomeTimelineState(error = result.message ?: "An unexpected error occurred")
                }

                is Resource.Loading -> {
                    HomeTimelineState(isLoading = true)
                }
            }

            println("lul")
            println(homeTimelineState.homeTimeline)
        }.launchIn(viewModelScope)

    }
}