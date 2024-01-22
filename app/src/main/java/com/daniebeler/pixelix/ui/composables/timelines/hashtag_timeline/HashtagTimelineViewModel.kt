package com.daniebeler.pixelix.ui.composables.timelines.hashtag_timeline

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniebeler.pixelix.common.Constants
import com.daniebeler.pixelix.common.Resource
import com.daniebeler.pixelix.domain.repository.CountryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class HashtagTimelineViewModel @Inject constructor(
    private val repository: CountryRepository
) : ViewModel() {

    var postsState by mutableStateOf(HashtagTimelineState())
    var hashtagState by mutableStateOf(HashtagState())

    fun getItemsFirstLoad(hashtag: String, refreshing: Boolean = false) {
        repository.getHashtagTimeline(hashtag).onEach { result ->
            postsState = when (result) {
                is Resource.Success -> {
                    val endReached =
                        (result.data?.size ?: 0) < Constants.HASHTAG_TIMELINE_POSTS_LIMIT
                    HashtagTimelineState(
                        hashtagTimeline = result.data ?: emptyList(),
                        error = "",
                        isLoading = false,
                        isRefreshing = false,
                        endReached = endReached
                    )
                }

                is Resource.Error -> {
                    HashtagTimelineState(
                        hashtagTimeline = postsState.hashtagTimeline,
                        error = result.message ?: "An unexpected error occurred",
                        isLoading = false,
                        isRefreshing = false
                    )
                }

                is Resource.Loading -> {
                    HashtagTimelineState(
                        hashtagTimeline = postsState.hashtagTimeline,
                        error = "",
                        isLoading = true,
                        isRefreshing = refreshing
                    )
                }
            }
        }.launchIn(viewModelScope)

    }

    fun getItemsPaginated(hashtag: String) {
        if (postsState.hashtagTimeline.isNotEmpty() && !postsState.isLoading && !postsState.endReached) {
            repository.getHashtagTimeline(hashtag, postsState.hashtagTimeline.last().id)
                .onEach { result ->
                    postsState = when (result) {
                        is Resource.Success -> {
                            val endReached =
                                (result.data?.size ?: 0) < Constants.HASHTAG_TIMELINE_POSTS_LIMIT
                            HashtagTimelineState(
                                hashtagTimeline = postsState.hashtagTimeline + (result.data
                                    ?: emptyList()),
                                error = "",
                                isLoading = false,
                                isRefreshing = false,
                                endReached = endReached
                            )
                        }

                        is Resource.Error -> {
                            HashtagTimelineState(
                                hashtagTimeline = postsState.hashtagTimeline,
                                error = result.message ?: "An unexpected error occurred",
                                isLoading = false,
                                isRefreshing = false
                            )
                        }

                        is Resource.Loading -> {
                            HashtagTimelineState(
                                hashtagTimeline = postsState.hashtagTimeline,
                                error = "",
                                isLoading = true,
                                isRefreshing = false
                            )
                        }
                    }
                }.launchIn(viewModelScope)
        }
    }

    fun refresh() {
        if (hashtagState.hashtag != null) {
            postsState = HashtagTimelineState()
            getItemsFirstLoad(hashtagState.hashtag!!.name, true)
        }
    }

    fun postGetsDeleted(postId: String) {
        postsState = postsState.copy(hashtagTimeline = postsState.hashtagTimeline.filter { post -> post.id != postId })
    }

    fun getHashtagInfo(hashtag: String) {
        repository.getHashtag(hashtag).onEach { result ->
            hashtagState = when (result) {
                is Resource.Success -> {
                    HashtagState(hashtag = result.data)
                }

                is Resource.Error -> {
                    HashtagState(error = result.message ?: "An unexpected error occurred")
                }

                is Resource.Loading -> {
                    HashtagState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun followHashtag(hashtag: String) {
        repository.followHashtag(hashtag).onEach { result ->
            hashtagState = when (result) {
                is Resource.Success -> {
                    HashtagState(hashtag = result.data)
                }

                is Resource.Error -> {
                    HashtagState(error = result.message ?: "An unexpected error occurred")
                }

                is Resource.Loading -> {
                    HashtagState(isLoading = true, hashtag = hashtagState.hashtag)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun unfollowHashtag(hashtag: String) {
        repository.unfollowHashtag(hashtag).onEach { result ->
            hashtagState = when (result) {
                is Resource.Success -> {
                    HashtagState(hashtag = result.data)
                }

                is Resource.Error -> {
                    HashtagState(error = result.message ?: "An unexpected error occurred")
                }

                is Resource.Loading -> {
                    HashtagState(isLoading = true, hashtag = hashtagState.hashtag)
                }
            }
        }.launchIn(viewModelScope)
    }
}