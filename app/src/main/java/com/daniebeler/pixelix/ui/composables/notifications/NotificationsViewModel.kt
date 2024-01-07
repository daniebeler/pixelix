package com.daniebeler.pixelix.ui.composables.notifications

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
class NotificationsViewModel @Inject constructor(
    private val repository: CountryRepository
): ViewModel() {

    var notificationsState by mutableStateOf(NotificationsState())
    init {
        getNotifications(false)
    }

    fun getNotifications(refreshing: Boolean) {
        repository.getNotifications().onEach { result ->
            notificationsState = when (result) {
                is Resource.Success -> {
                    NotificationsState(notifications = result.data ?: emptyList())
                }

                is Resource.Error -> {
                    NotificationsState(error = result.message ?: "An unexpected error occurred")
                }

                is Resource.Loading -> {
                    NotificationsState(isLoading = true, refreshing = refreshing)
                }
            }
        }.launchIn(viewModelScope)
    }
}