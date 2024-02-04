package com.daniebeler.pixelix.ui.composables.settings.followed_hashtags

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniebeler.pixelix.common.Resource
import com.daniebeler.pixelix.domain.model.Tag
import com.daniebeler.pixelix.domain.repository.CountryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class FollowedHashtagsViewModel @Inject constructor(
    private val repository: CountryRepository
) : ViewModel() {

    var followedHashtagsState by mutableStateOf(FollowedHashtagsState())

    init {
        getFollowedHashtags()
    }

    fun getFollowedHashtags(refreshing: Boolean = false) {
        repository.getFollowedHashtags().onEach { result ->
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
        repository.getHashtag(tag.name).onEach { result ->
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