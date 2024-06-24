package com.daniebeler.pfpixelix.domain.repository

import com.daniebeler.pfpixelix.domain.model.AuthData
import com.daniebeler.pfpixelix.domain.model.LoginData

interface AuthRepository {
    suspend fun addNewLoginData(newLoginData: LoginData)
    suspend fun updateOngoingLoginData(newLoginData: LoginData)
    suspend fun finishLogin(newLoginData: LoginData, currentlyLoggedIn: String)
    suspend fun updateCurrentUser(accountId: String)
    suspend fun getAuthData(): AuthData
    suspend fun getOngoingLogin(): LoginData?
    suspend fun getCurrentLoginData(): LoginData?
    suspend fun logout()
    suspend fun removeLoginData(accountId: String)
}