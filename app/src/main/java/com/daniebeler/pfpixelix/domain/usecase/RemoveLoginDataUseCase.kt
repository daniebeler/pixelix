package com.daniebeler.pfpixelix.domain.usecase

import com.daniebeler.pfpixelix.domain.repository.AuthRepository

class RemoveLoginDataUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(accountId: String) {
        authRepository.removeLoginData(accountId)
    }
}