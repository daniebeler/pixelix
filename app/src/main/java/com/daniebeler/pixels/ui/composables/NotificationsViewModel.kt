package com.daniebeler.pixels.ui.composables

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniebeler.pixels.api.CountryRepository
import com.daniebeler.pixels.domain.model.Notification
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val repository: CountryRepository
): ViewModel() {

    var notifications: List<Notification> by mutableStateOf(emptyList())

    init {
        viewModelScope.launch {
            notifications = repository.getNotifications()
        }
    }
}