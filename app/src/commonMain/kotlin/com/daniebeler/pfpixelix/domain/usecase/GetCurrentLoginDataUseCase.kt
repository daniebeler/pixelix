package com.daniebeler.pfpixelix.domain.usecase

import com.daniebeler.pfpixelix.domain.service.session.AuthService
import com.daniebeler.pfpixelix.domain.service.session.Credentials
import me.tatarka.inject.annotations.Inject

@Inject
class GetCurrentLoginDataUseCase constructor(private val authService: AuthService) {
    suspend operator fun invoke(): Credentials? {
        return authService.getCurrentSession()
    }
}