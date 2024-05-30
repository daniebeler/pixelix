package com.daniebeler.pfpixelix.ui.composables.profile.own_profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniebeler.pfpixelix.domain.model.AuthData
import com.daniebeler.pfpixelix.domain.usecase.GetAuthDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountSwitchViewModel @Inject constructor(
    private val getAuthDataUseCase: GetAuthDataUseCase
): ViewModel() {
    var authData by mutableStateOf(AuthData())

    init {
        viewModelScope.launch {
            authData = getAuthDataUseCase()
        }
    }
}