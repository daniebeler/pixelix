package com.daniebeler.pixelix.ui.composables.timelines.local_timeline

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniebeler.pixelix.common.Resource
import com.daniebeler.pixelix.domain.repository.CountryRepository
import com.daniebeler.pixelix.ui.composables.timelines.home_timeline.HomeTimelineState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class LocalTimelineViewModel @Inject constructor(
    private val repository: CountryRepository
): ViewModel() {

    var localTimelineState by mutableStateOf(LocalTimelineState())

    init {
        getItemsFirstLoad(false)
    }

    private fun getItemsFirstLoad(refreshing: Boolean) {
        repository.getLocalTimeline().onEach { result ->
            localTimelineState = when (result) {
                is Resource.Success -> {
                    LocalTimelineState(
                        localTimeline = result.data ?: emptyList(),
                        error = "",
                        isLoading = false,
                        refreshing = false
                    )
                }

                is Resource.Error -> {
                    LocalTimelineState(
                        localTimeline = localTimelineState.localTimeline,
                        error = result.message ?: "An unexpected error occurred",
                        isLoading = false,
                        refreshing = false
                    )
                }

                is Resource.Loading -> {
                    LocalTimelineState(
                        localTimeline = localTimelineState.localTimeline,
                        error = "",
                        isLoading = true,
                        refreshing = refreshing
                    )
                }
            }
        }.launchIn(viewModelScope)

    }

    fun getItemsPaginated() {
        if (localTimelineState.localTimeline.isNotEmpty() && !localTimelineState.isLoading) {
            repository.getLocalTimeline(localTimelineState.localTimeline.last().id).onEach { result ->
                localTimelineState = when (result) {
                    is Resource.Success -> {
                        LocalTimelineState(
                            localTimeline = localTimelineState.localTimeline + (result.data
                                ?: emptyList()),
                            error = "",
                            isLoading = false,
                            refreshing = false
                        )
                    }

                    is Resource.Error -> {
                        LocalTimelineState(
                            localTimeline = localTimelineState.localTimeline,
                            error = result.message ?: "An unexpected error occurred",
                            isLoading = false,
                            refreshing = false
                        )
                    }

                    is Resource.Loading -> {
                        LocalTimelineState(
                            localTimeline = localTimelineState.localTimeline,
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
        println("delete post $postId filter")
        localTimelineState = localTimelineState.copy(localTimeline = localTimelineState.localTimeline.filter { post -> post.id != postId })
    }
}