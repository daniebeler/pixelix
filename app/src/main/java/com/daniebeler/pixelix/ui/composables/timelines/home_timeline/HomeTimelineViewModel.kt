package com.daniebeler.pixelix.ui.composables.timelines.home_timeline

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniebeler.pixelix.common.Resource
import com.daniebeler.pixelix.domain.repository.CountryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class HomeTimelineViewModel @Inject constructor(
    private val repository: CountryRepository
) : ViewModel() {

    var homeTimelineState by mutableStateOf(HomeTimelineState())

    init {
        loadMorePosts(false)
    }

    fun loadMorePosts(refreshing: Boolean) {

        val maxId = if (homeTimelineState.homeTimeline.isEmpty()) {
            ""
        } else {
            homeTimelineState.homeTimeline.last().id
        }

        repository.getHomeTimeline(maxId).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    homeTimelineState = homeTimelineState.copy(
                        homeTimeline = homeTimelineState.homeTimeline + (result.data
                            ?: emptyList()),
                        error = "",
                        isLoading = false,
                        refreshing = false
                    )
                }

                is Resource.Error -> {
                    homeTimelineState = homeTimelineState.copy(
                        homeTimeline = homeTimelineState.homeTimeline,
                        error = result.message ?: "An unexpected error occurred",
                        isLoading = false,
                        refreshing = false
                    )
                }

                is Resource.Loading -> {
                    homeTimelineState = homeTimelineState.copy(
                        homeTimeline = homeTimelineState.homeTimeline,
                        error = "",
                        isLoading = true,
                        refreshing = refreshing
                    )
                }
            }
        }.launchIn(viewModelScope)

    }

    fun refresh() {
        homeTimelineState = HomeTimelineState()
        loadMorePosts(true)
    }
}