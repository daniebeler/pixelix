package com.daniebeler.pfpixelix.ui.composables.timelines.home_timeline

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.usecase.GetHomeTimelineUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class HomeTimelineViewModel @Inject constructor(
    private val getHomeTimelineUseCase: GetHomeTimelineUseCase
) : ViewModel() {

    var homeTimelineState by mutableStateOf(HomeTimelineState())

    init {
        getItemsFirstLoad(false)
    }

    private fun getItemsFirstLoad(refreshing: Boolean) {
        getHomeTimelineUseCase().onEach { result ->
            homeTimelineState = when (result) {
                is Resource.Success -> {
                    HomeTimelineState(
                        homeTimeline = result.data ?: emptyList(),
                        error = "",
                        isLoading = false,
                        refreshing = false
                    )
                }

                is Resource.Error -> {
                    HomeTimelineState(
                        homeTimeline = homeTimelineState.homeTimeline,
                        error = result.message ?: "An unexpected error occurred",
                        isLoading = false,
                        refreshing = false
                    )
                }

                is Resource.Loading -> {
                    HomeTimelineState(
                        homeTimeline = homeTimelineState.homeTimeline,
                        error = "",
                        isLoading = true,
                        refreshing = refreshing
                    )
                }
            }
        }.launchIn(viewModelScope)

    }

    fun getItemsPaginated() {
        if (homeTimelineState.homeTimeline.isNotEmpty() && !homeTimelineState.isLoading) {
            getHomeTimelineUseCase(homeTimelineState.homeTimeline.last().id).onEach { result ->
                homeTimelineState = when (result) {
                    is Resource.Success -> {
                        HomeTimelineState(
                            homeTimeline = homeTimelineState.homeTimeline + (result.data
                                ?: emptyList()), error = "", isLoading = false, refreshing = false
                        )
                    }

                    is Resource.Error -> {
                        HomeTimelineState(
                            homeTimeline = homeTimelineState.homeTimeline,
                            error = result.message ?: "An unexpected error occurred",
                            isLoading = false,
                            refreshing = false
                        )
                    }

                    is Resource.Loading -> {
                        HomeTimelineState(
                            homeTimeline = homeTimelineState.homeTimeline,
                            error = "",
                            isLoading = true,
                            refreshing = false
                        )
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    fun refresh() {
        getItemsFirstLoad(true)
    }

    fun postGetsDeleted(postId: String) {
        homeTimelineState =
            homeTimelineState.copy(homeTimeline = homeTimelineState.homeTimeline.filter { post -> post.id != postId })
    }
}