package com.daniebeler.pfpixelix.ui.composables.profile.own_profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniebeler.pfpixelix.domain.model.LoginData
import com.daniebeler.pfpixelix.domain.usecase.GetAuthDataUseCase
import com.daniebeler.pfpixelix.domain.usecase.RemoveLoginDataUseCase
import com.daniebeler.pfpixelix.domain.usecase.UpdateCurrentUserUseCase
import com.daniebeler.pfpixelix.utils.Navigate
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject

class AccountSwitchViewModel @Inject constructor(
    private val getAuthDataUseCase: GetAuthDataUseCase,
    private val updateCurrentUserUseCase: UpdateCurrentUserUseCase,
    private val removeLoginDataUseCase: RemoveLoginDataUseCase
) : ViewModel() {
    var currentlyLoggedIn: CurrentAccountState by mutableStateOf(CurrentAccountState())
    var otherAccounts: OtherAccountsState by mutableStateOf(OtherAccountsState())

    init {
        loadAccounts()
    }

    private fun loadAccounts() {
        viewModelScope.launch {
            val authData = getAuthDataUseCase()
            currentlyLoggedIn =
                CurrentAccountState(currentAccount = authData.loginDataList.find { it.accountId == authData.currentlyLoggedIn }
                    ?: LoginData())
            otherAccounts =
                OtherAccountsState(otherAccounts = authData.loginDataList.filter { it.accountId != authData.currentlyLoggedIn && !it.loginOngoing })
        }
    }

    fun switchAccount(newAccount: LoginData, changedAccount: () -> Unit) {
        val coroutine = viewModelScope.launch {
            updateCurrentUserUseCase(newAccount)
        }

        coroutine.invokeOnCompletion {
            Navigate.changeAccount()
            changedAccount()
            loadAccounts()
        }
    }

    fun removeAccount(accountId: String) {
        val coroutine = viewModelScope.launch {
            removeLoginDataUseCase(accountId)
        }
        coroutine.invokeOnCompletion {
            loadAccounts()
        }
    }
}
