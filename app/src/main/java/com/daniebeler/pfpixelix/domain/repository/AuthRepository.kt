package com.daniebeler.pfpixelix.domain.repository

import com.daniebeler.pfpixelix.domain.model.LoginData

interface AuthRepository {
    suspend fun addNewLoginData(newLoginData: LoginData)
    suspend fun updateOngoingLoginData(newLoginData: LoginData, currentlyLoggedIn: String = "")
    suspend fun getOngoingLogin(): LoginData?
    suspend fun getCurrentLoginData(): LoginData?
    suspend fun logout()
}