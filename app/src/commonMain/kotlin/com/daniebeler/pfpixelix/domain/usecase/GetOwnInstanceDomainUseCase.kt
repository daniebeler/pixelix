package com.daniebeler.pfpixelix.domain.usecase

import com.daniebeler.pfpixelix.domain.service.session.AuthService
import me.tatarka.inject.annotations.Inject

@Inject
class GetOwnInstanceDomainUseCase(
    private val authService: AuthService
) {
    suspend operator fun invoke(): String {
        val currentLoginData = authService.getCurrentSession()!!
        return currentLoginData.serverUrl
    }
}