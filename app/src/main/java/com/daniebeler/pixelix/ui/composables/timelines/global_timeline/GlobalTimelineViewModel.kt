package com.daniebeler.pixelix.ui.composables.timelines.global_timeline

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
class GlobalTimelineViewModel @Inject constructor(
    private val repository: CountryRepository
): ViewModel() {

    var globalTimelineState by mutableStateOf(GlobalTimelineState())

    init {
        loadMorePosts(false)
    }

    fun loadMorePosts(refreshing: Boolean) {

        val maxId = if (globalTimelineState.globalTimeline.isEmpty()) {
            ""
        } else {
            globalTimelineState.globalTimeline.last().id
        }

        repository.getGlobalTimeline(maxId).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    globalTimelineState = globalTimelineState.copy(
                        globalTimeline = globalTimelineState.globalTimeline + (result.data
                            ?: emptyList()),
                        error = "",
                        isLoading = false,
                        refreshing = false
                    )
                }

                is Resource.Error -> {
                    globalTimelineState = globalTimelineState.copy(
                        globalTimeline = globalTimelineState.globalTimeline,
                        error = result.message ?: "An unexpected error occurred",
                        isLoading = false,
                        refreshing = false
                    )
                }

                is Resource.Loading -> {
                    globalTimelineState = globalTimelineState.copy(
                        globalTimeline = globalTimelineState.globalTimeline,
                        error = "",
                        isLoading = true,
                        refreshing = refreshing
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    fun refresh() {
        globalTimelineState = GlobalTimelineState()
        loadMorePosts(true)
    }
}