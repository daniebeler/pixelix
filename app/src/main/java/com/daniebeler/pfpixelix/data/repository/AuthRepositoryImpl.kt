package com.daniebeler.pfpixelix.data.repository

import androidx.datastore.core.DataStore
import com.daniebeler.pfpixelix.domain.model.AuthData
import com.daniebeler.pfpixelix.domain.model.LoginData
import com.daniebeler.pfpixelix.domain.repository.AuthRepository
import kotlinx.coroutines.flow.first
import me.tatarka.inject.annotations.Inject

class AuthRepositoryImpl @Inject constructor(private val dataStore: DataStore<AuthData>) :
    AuthRepository {
    override suspend fun addNewLoginData(newLoginData: LoginData) {
        try {
            dataStore.updateData { authData ->
                authData.copy(
                    loginDataList = authData.loginDataList + newLoginData
                )
            }
        } catch (e: Exception) {
            println(e)
        }
    }

    override suspend fun updateOngoingLoginData(
        newLoginData: LoginData
    ) {
        dataStore.updateData { authData ->
            var updatedLoginDataList = authData.loginDataList

            updatedLoginDataList = updatedLoginDataList.map { loginData ->
                if (loginData.loginOngoing) {
                    newLoginData
                } else {
                    loginData
                }
            }

            authData.copy(
                loginDataList = updatedLoginDataList
            )
        }
    }

    override suspend fun finishLogin(
        newLoginData: LoginData, currentlyLoggedIn: String
    ) {
        dataStore.updateData { authData ->
            var updatedLoginDataList = authData.loginDataList
            updatedLoginDataList =
                updatedLoginDataList.filter { it.accountId != newLoginData.accountId && it.accountId.isNotBlank() && !it.loginOngoing }


            updatedLoginDataList = updatedLoginDataList + newLoginData

            authData.copy(
                loginDataList = updatedLoginDataList, currentlyLoggedIn = currentlyLoggedIn
            )
        }
    }

    override suspend fun updateCurrentUser(accountId: String) {
        dataStore.updateData { authData ->
            if (authData.loginDataList.any { it.accountId == accountId }) {
                authData.copy(currentlyLoggedIn = accountId)
            } else {
                authData
            }
        }
    }

    override suspend fun getAuthData(): AuthData {
        return dataStore.data.first()
    }

    override suspend fun getOngoingLogin(): LoginData? {
        return dataStore.data.first().loginDataList.find { it.loginOngoing }
    }

    override suspend fun getCurrentLoginData(): LoginData? {
        val currentlyLoggedIn = dataStore.data.first().currentlyLoggedIn
        return dataStore.data.first().loginDataList.find { it.accountId == currentlyLoggedIn }
    }

    override suspend fun logout() {
        dataStore.updateData { authData ->
            authData.copy(loginDataList = authData.loginDataList.filter { loginData -> loginData.accountId != authData.currentlyLoggedIn }, currentlyLoggedIn = "")
        }
    }

    override suspend fun removeLoginData(accountId: String) {
        dataStore.updateData { authData ->
            authData.copy(loginDataList = authData.loginDataList.filter { loginData -> loginData.accountId != accountId })
        }
    }
}