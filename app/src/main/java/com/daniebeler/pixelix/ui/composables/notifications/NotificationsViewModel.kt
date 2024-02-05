package com.daniebeler.pixelix.ui.composables.notifications

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniebeler.pixelix.common.Constants
import com.daniebeler.pixelix.common.Resource
import com.daniebeler.pixelix.domain.repository.CountryRepository
import com.daniebeler.pixelix.domain.usecase.GetNotifications
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val getNotifications: GetNotifications
) : ViewModel() {

    var notificationsState by mutableStateOf(NotificationsState())

    init {
        getNotificationsFirstLoad(false)
    }

    private fun getNotificationsFirstLoad(refreshing: Boolean) {
        getNotifications().onEach { result ->
            notificationsState = when (result) {
                is Resource.Success -> {
                    val endReached = (result.data?.size ?: 0) < Constants.NOTIFICATIONS_LIMIT
                    NotificationsState(notifications = result.data ?: emptyList(), endReached = endReached)
                }

                is Resource.Error -> {
                    NotificationsState(error = result.message ?: "An unexpected error occurred")
                }

                is Resource.Loading -> {
                    NotificationsState(
                        isLoading = true,
                        isRefreshing = refreshing,
                        notifications = notificationsState.notifications
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getNotificationsPaginated() {
        if (notificationsState.notifications.isNotEmpty() && !notificationsState.isLoading && !notificationsState.endReached) {
            getNotifications(notificationsState.notifications.last().id).onEach { result ->
                notificationsState = when (result) {
                    is Resource.Success -> {
                        val endReached = (result.data?.size ?: 0) < Constants.NOTIFICATIONS_LIMIT
                        NotificationsState(
                            notifications = notificationsState.notifications + (result.data
                                ?: emptyList()),
                            endReached = endReached
                        )
                    }

                    is Resource.Error -> {
                        NotificationsState(error = result.message ?: "An unexpected error occurred")
                    }

                    is Resource.Loading -> {
                        NotificationsState(
                            isLoading = true,
                            isRefreshing = false,
                            notifications = notificationsState.notifications
                        )
                    }
                }
            }.launchIn(viewModelScope)
        }

    }

    fun refresh() {
        getNotificationsFirstLoad(true)
    }
}