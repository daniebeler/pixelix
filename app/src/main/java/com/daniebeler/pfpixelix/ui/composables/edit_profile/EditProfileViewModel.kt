package com.daniebeler.pfpixelix.ui.composables.edit_profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.usecase.GetOwnAccountUseCase
import com.daniebeler.pfpixelix.ui.composables.profile.AccountState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val getOwnAccountUseCase: GetOwnAccountUseCase
) : ViewModel() {

    var accountState by mutableStateOf(EditProfileState())

    var displayname by mutableStateOf("")

    init {
        getAccount()
    }

    private fun getAccount() {
        getOwnAccountUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    accountState = EditProfileState(account = result.data)
                    displayname = accountState.account?.displayname ?: ""
                }

                is Resource.Error -> {
                    accountState = EditProfileState(error = result.message ?: "An unexpected error occurred")
                }

                is Resource.Loading -> {
                    accountState = EditProfileState(isLoading = true, account = accountState.account)
                }
            }
        }.launchIn(viewModelScope)
    }
}