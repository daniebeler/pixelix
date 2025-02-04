package com.daniebeler.pfpixelix.domain.usecase

import com.daniebeler.pfpixelix.di.HostSelectionInterceptorInterface
import com.daniebeler.pfpixelix.domain.model.LoginData
import com.daniebeler.pfpixelix.domain.repository.AuthRepository
import me.tatarka.inject.annotations.Inject

@Inject
class AddNewLoginUseCase(
    private val authRepository: AuthRepository,
    private val hostSelectionInterceptorInterface: HostSelectionInterceptorInterface
) {
    suspend operator fun invoke(loginData: LoginData) {
        if (loginData.baseUrl.isNotBlank()) {
            hostSelectionInterceptorInterface.setHost(loginData.baseUrl.replace("https://", ""))        }
        if (loginData.accessToken.isNotBlank()) {
            hostSelectionInterceptorInterface.setToken(loginData.accessToken)
        }
        authRepository.addNewLoginData(loginData)
    }
}