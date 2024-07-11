package com.daniebeler.pfpixelix.ui.composables.stories

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.model.Story
import com.daniebeler.pfpixelix.domain.usecase.GetOwnInstanceDomainUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetStoriesCarouselUseCase
import com.daniebeler.pfpixelix.domain.usecase.OpenExternalUrlUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreviewRowViewModel @Inject constructor(
    private val getStoriesCarouselUseCase: GetStoriesCarouselUseCase,
    private val getOwnInstanceDomainUseCase: GetOwnInstanceDomainUseCase,
    private val openExternalUrlUseCase: OpenExternalUrlUseCase
) : ViewModel() {

    var storiesCarousel by mutableStateOf(StoriesCarouselState())

    var showStory by mutableStateOf(false)

    var showMyStory by mutableStateOf(false)

    init {
        getStoriesCarousel()
    }

    private fun getStoriesCarousel() {
        getStoriesCarouselUseCase().onEach { result ->
            storiesCarousel = when (result) {
                is Resource.Success -> {
                    StoriesCarouselState(carousel = result.data)
                }

                is Resource.Error -> {
                    StoriesCarouselState(error = result.message ?: "An unexpected error occurred")
                }

                is Resource.Loading -> {
                    StoriesCarouselState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun openNewStoryPage(context: Context) {
        viewModelScope.launch {
            val domain = getOwnInstanceDomainUseCase().first()
            val moreSettingUrl = "https://$domain/i/stories/new"
            openExternalUrlUseCase(context, moreSettingUrl)
        }
    }

}