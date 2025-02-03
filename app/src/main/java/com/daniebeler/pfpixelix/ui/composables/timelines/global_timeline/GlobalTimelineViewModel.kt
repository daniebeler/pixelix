package com.daniebeler.pfpixelix.ui.composables.timelines.global_timeline

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.usecase.GetGlobalTimelineUseCase
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import me.tatarka.inject.annotations.Inject

class GlobalTimelineViewModel @Inject constructor(
    private val getGlobalTimelineUseCase: GetGlobalTimelineUseCase
) : ViewModel() {

    var globalTimelineState by mutableStateOf(GlobalTimelineState())

    init {
        getItemsFirstLoad(false)
    }

    private fun getItemsFirstLoad(refreshing: Boolean) {
        getGlobalTimelineUseCase().onEach { result ->
            globalTimelineState = when (result) {
                is Resource.Success -> {
                    GlobalTimelineState(
                        globalTimeline = result.data ?: emptyList(),
                        error = "",
                        isLoading = false,
                        refreshing = false
                    )
                }

                is Resource.Error -> {
                    GlobalTimelineState(
                        globalTimeline = globalTimelineState.globalTimeline,
                        error = result.message ?: "An unexpected error occurred",
                        isLoading = false,
                        refreshing = false
                    )
                }

                is Resource.Loading -> {
                    GlobalTimelineState(
                        globalTimeline = globalTimelineState.globalTimeline,
                        error = "",
                        isLoading = true,
                        refreshing = refreshing
                    )
                }
            }
        }.launchIn(viewModelScope)

    }

    fun getItemsPaginated() {
        if (globalTimelineState.globalTimeline.isNotEmpty() && !globalTimelineState.isLoading) {
            getGlobalTimelineUseCase(globalTimelineState.globalTimeline.last().id).onEach { result ->
                globalTimelineState = when (result) {
                    is Resource.Success -> {
                        GlobalTimelineState(
                            globalTimeline = globalTimelineState.globalTimeline + (result.data
                                ?: emptyList()), error = "", isLoading = false, refreshing = false
                        )
                    }

                    is Resource.Error -> {
                        GlobalTimelineState(
                            globalTimeline = globalTimelineState.globalTimeline,
                            error = result.message ?: "An unexpected error occurred",
                            isLoading = false,
                            refreshing = false
                        )
                    }

                    is Resource.Loading -> {
                        GlobalTimelineState(
                            globalTimeline = globalTimelineState.globalTimeline,
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
        globalTimelineState =
            globalTimelineState.copy(globalTimeline = globalTimelineState.globalTimeline.filter { post -> post.id != postId })
    }
}