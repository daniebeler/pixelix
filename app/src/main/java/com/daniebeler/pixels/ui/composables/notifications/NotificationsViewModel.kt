package com.daniebeler.pixels.ui.composables.notifications

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniebeler.pixels.common.Resource
import com.daniebeler.pixels.domain.repository.CountryRepository
import com.daniebeler.pixels.domain.model.Notification
import com.daniebeler.pixels.ui.composables.trending.trending_posts.TrendingPostsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val repository: CountryRepository
): ViewModel() {

    var notificationsState by mutableStateOf(NotificationsState())

    init {
        getNotifications()
    }

    private fun getNotifications() {
        repository.getNotifications().onEach { result ->
            notificationsState = when (result) {
                is Resource.Success -> {
                    NotificationsState(notifications = result.data ?: emptyList())
                }

                is Resource.Error -> {
                    NotificationsState(error = result.message ?: "An unexpected error occurred")
                }

                is Resource.Loading -> {
                    NotificationsState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}