package com.daniebeler.pfpixelix.ui.composables.profile.own_profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniebeler.pfpixelix.domain.model.LoginData
import com.daniebeler.pfpixelix.domain.usecase.GetAuthDataUseCase
import com.daniebeler.pfpixelix.domain.usecase.UpdateCurrentUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AccountSwitchViewModel @Inject constructor(
    private val getAuthDataUseCase: GetAuthDataUseCase,
    private val updateCurrentUserUseCase: UpdateCurrentUserUseCase
) : ViewModel() {
    var currentlyLoggedIn: CurrentAccountState by mutableStateOf(CurrentAccountState())
    var otherAccounts: OtherAccountsState by mutableStateOf(OtherAccountsState())

    init {
        viewModelScope.launch {
            val authData = getAuthDataUseCase()
            currentlyLoggedIn =
                CurrentAccountState(currentAccount = authData.loginDataList.find { it.accountId == authData.currentlyLoggedIn }
                    ?: LoginData())
            otherAccounts =
                OtherAccountsState(otherAccounts = authData.loginDataList.filter { it.accountId != authData.currentlyLoggedIn })
        }
    }

    fun switchAccount(newAccount: LoginData) {
        viewModelScope.launch {
            updateCurrentUserUseCase(newAccount)
        }
    }
}
