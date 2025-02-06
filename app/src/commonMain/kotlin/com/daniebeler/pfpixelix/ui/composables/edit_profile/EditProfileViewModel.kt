package com.daniebeler.pfpixelix.ui.composables.edit_profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.usecase.GetOwnAccountUseCase
import com.daniebeler.pfpixelix.domain.usecase.UpdateAccountUseCase
import com.daniebeler.pfpixelix.utils.EmptyKmpUri
import com.daniebeler.pfpixelix.utils.KmpContext
import com.daniebeler.pfpixelix.utils.KmpUri
import com.daniebeler.pfpixelix.utils.toKmpUri
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import me.tatarka.inject.annotations.Inject

class EditProfileViewModel @Inject constructor(
    private val getOwnAccountUseCase: GetOwnAccountUseCase,
    private val updateAccountUseCase: UpdateAccountUseCase
) : ViewModel() {

    var accountState by mutableStateOf(EditProfileState())

    var firstLoaded by mutableStateOf(false)
    var displayname by mutableStateOf("")
    var note by mutableStateOf("")
    var website by mutableStateOf("")
    var avatarUri by mutableStateOf(EmptyKmpUri)
    var avatarChanged by mutableStateOf(false)
    var privateProfile by mutableStateOf(false)
    init {
        getAccount()
    }

    private fun getAccount() {
        getOwnAccountUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    accountState = EditProfileState(account = result.data)
                    displayname = accountState.account?.displayname ?: ""
                    note = accountState.account?.note ?: ""
                    website = accountState.account?.website?.replace("https://", "") ?: ""
                    avatarUri = accountState.account?.avatar!!.toKmpUri()
                    privateProfile = accountState.account?.locked ?: false
                    firstLoaded = true
                }

                is Resource.Error -> {
                    accountState =
                        EditProfileState(error = result.message ?: "An unexpected error occurred")
                }

                is Resource.Loading -> {
                    accountState =
                        EditProfileState(isLoading = true, account = accountState.account)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun save(context: KmpContext) {
        val newAvatarUri: KmpUri? = if (avatarUri == accountState.account?.avatar?.toKmpUri()) {
            null
        } else {
            avatarUri
        }
        updateAccountUseCase(
            displayname, note, "https://$website", privateProfile, newAvatarUri, context
        ).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    accountState = EditProfileState(account = result.data)
                    avatarChanged = false
                }

                is Resource.Error -> {
                    accountState =
                        EditProfileState(error = result.message ?: "An unexpected error occurred")
                }

                is Resource.Loading -> {
                    accountState =
                        EditProfileState(isLoading = true, account = accountState.account)
                }
            }
        }.launchIn(viewModelScope)
    }
}