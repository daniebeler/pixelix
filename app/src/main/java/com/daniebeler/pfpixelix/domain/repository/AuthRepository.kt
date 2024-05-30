package com.daniebeler.pfpixelix.domain.repository

import com.daniebeler.pfpixelix.domain.model.AuthData
import com.daniebeler.pfpixelix.domain.model.LoginData

interface AuthRepository {
    suspend fun addNewLoginData(newLoginData: LoginData)
    suspend fun updateOngoingLoginData(newLoginData: LoginData, currentlyLoggedIn: String = "")
    suspend fun getAuthData(): AuthData
    suspend fun getOngoingLogin(): LoginData?
    suspend fun getCurrentLoginData(): LoginData?
    suspend fun logout()
}