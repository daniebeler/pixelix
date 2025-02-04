package com.daniebeler.pfpixelix.ui.composables.settings.followed_hashtags

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.model.Tag
import com.daniebeler.pfpixelix.domain.usecase.GetFollowedHashtagsUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetHashtagUseCase
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import me.tatarka.inject.annotations.Inject

class FollowedHashtagsViewModel @Inject constructor(
    private val getFollowedHashtagsUseCase: GetFollowedHashtagsUseCase,
    private val getHashtagUseCase: GetHashtagUseCase
) : ViewModel() {

    var followedHashtagsState by mutableStateOf(FollowedHashtagsState())

    init {
        getFollowedHashtags()
    }

    fun getFollowedHashtags(refreshing: Boolean = false) {
        getFollowedHashtagsUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    if (result.data?.isNotEmpty() == false) {
                        followedHashtagsState = FollowedHashtagsState()
                    } else {
                        result.data!!.forEach {
                            getFollowedHashtagSingle(it)
                        }
                    }
                }

                is Resource.Error -> {
                    followedHashtagsState = FollowedHashtagsState(
                        error = result.message ?: "An unexpected error occurred"
                    )
                }

                is Resource.Loading -> {
                    followedHashtagsState = FollowedHashtagsState(
                        isLoading = true,
                        isRefreshing = refreshing,
                        followedHashtags = followedHashtagsState.followedHashtags
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getFollowedHashtagSingle(tag: Tag) {
        getHashtagUseCase(tag.name).onEach { result ->
            followedHashtagsState = when (result) {
                is Resource.Success -> {
                    if (result.data != null) {
                        FollowedHashtagsState(followedHashtags = followedHashtagsState.followedHashtags + result.data)
                    } else {
                        FollowedHashtagsState(followedHashtags = followedHashtagsState.followedHashtags)
                    }
                }

                is Resource.Error -> {
                    FollowedHashtagsState(error = result.message ?: "An unexpected error occurred")
                }

                is Resource.Loading -> {
                    FollowedHashtagsState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

}