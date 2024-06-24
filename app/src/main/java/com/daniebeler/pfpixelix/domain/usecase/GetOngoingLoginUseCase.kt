package com.daniebeler.pfpixelix.domain.usecase

import com.daniebeler.pfpixelix.domain.model.LoginData
import com.daniebeler.pfpixelix.domain.repository.AuthRepository

class GetOngoingLoginUseCase constructor(private val authRepository: AuthRepository) {
    suspend operator fun invoke(): LoginData? {
        return authRepository.getOngoingLogin()
    }
}