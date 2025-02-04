package com.daniebeler.pfpixelix.ui.composables.timelines.hashtag_timeline

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniebeler.pfpixelix.common.Constants
import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.model.RelatedHashtag
import com.daniebeler.pfpixelix.domain.model.Tag
import com.daniebeler.pfpixelix.domain.usecase.FollowHashtagUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetHashtagTimelineUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetHashtagUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetRelatedHashtagsUseCase
import com.daniebeler.pfpixelix.domain.usecase.UnfollowHashtagUseCase
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import me.tatarka.inject.annotations.Inject

class HashtagTimelineViewModel @Inject constructor(
    private val getHashtagUseCase: GetHashtagUseCase,
    private val followHashtagUseCase: FollowHashtagUseCase,
    private val unfollowHashtagUseCase: UnfollowHashtagUseCase,
    private val getHashtagTimelineUseCase: GetHashtagTimelineUseCase,
    private val getRelatedHashtagsUseCase: GetRelatedHashtagsUseCase
) : ViewModel() {

    var postsState by mutableStateOf(HashtagTimelineState())
    var hashtagState by mutableStateOf(HashtagState())

    var relatedHashtags by mutableStateOf<List<RelatedHashtag>>(emptyList())

    fun getItemsFirstLoad(hashtag: String, refreshing: Boolean = false) {
        getHashtagTimelineUseCase(hashtag).onEach { result ->
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
            getHashtagTimelineUseCase(
                hashtag, postsState.hashtagTimeline.last().id
            ).onEach { result ->
                postsState = when (result) {
                    is Resource.Success -> {
                        val endReached = (result.data?.size ?: 0) == 0
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

    fun getRelatedHashtags(hashtag: String) {
        getRelatedHashtagsUseCase(hashtag).onEach { result ->
            if (result is Resource.Success) {
                relatedHashtags = result.data ?: emptyList()
                println("juhuu" + result.data)
            } else {
                println("fief" + result.message)
            }
        }.launchIn(viewModelScope)
    }

    fun refresh() {
        if (hashtagState.hashtag != null) {
            postsState = HashtagTimelineState()
            getItemsFirstLoad(hashtagState.hashtag!!.name, true)
        }
    }

    fun postGetsDeleted(postId: String) {
        postsState =
            postsState.copy(hashtagTimeline = postsState.hashtagTimeline.filter { post -> post.id != postId })
    }

    fun getHashtagInfo(hashtag: String) {
        getHashtagUseCase(hashtag).onEach { result ->
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
        followHashtagUseCase(hashtag).onEach { result ->
            hashtagState = when (result) {
                is Resource.Success -> {
                    val newHashtag = hashtagState.hashtag
                    if (newHashtag != null) {
                        newHashtag.following = true
                        HashtagState(hashtag = newHashtag)
                    } else {
                        HashtagState(hashtag = result.data)
                    }
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
        unfollowHashtagUseCase(hashtag).onEach { result ->
            hashtagState = when (result) {
                is Resource.Success -> {
                    val newHashtag = hashtagState.hashtag
                    if (newHashtag != null) {
                        newHashtag.following = false
                        HashtagState(hashtag = newHashtag)
                    } else {
                        HashtagState(hashtag = result.data)
                    }
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