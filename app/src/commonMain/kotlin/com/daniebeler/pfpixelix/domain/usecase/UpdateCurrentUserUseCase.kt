package com.daniebeler.pfpixelix.domain.usecase

import com.daniebeler.pfpixelix.di.HostSelectionInterceptorInterface
import com.daniebeler.pfpixelix.domain.model.LoginData
import com.daniebeler.pfpixelix.domain.repository.AuthRepository
import me.tatarka.inject.annotations.Inject

@Inject
class UpdateCurrentUserUseCase(private val authRepository: AuthRepository, private val hostSelectionInterceptorInterface: HostSelectionInterceptorInterface) {
    suspend operator fun invoke(newLoginData: LoginData) {
        hostSelectionInterceptorInterface.setHost(newLoginData.baseUrl)
        hostSelectionInterceptorInterface.setToken(newLoginData.accessToken)
        authRepository.updateCurrentUser(newLoginData.accountId)
    }
}