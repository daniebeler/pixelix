package com.daniebeler.pfpixelix.domain.usecase

import com.daniebeler.pfpixelix.domain.model.AuthData
import com.daniebeler.pfpixelix.domain.repository.AuthRepository
import me.tatarka.inject.annotations.Inject

@Inject
class GetAuthDataUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(): AuthData {
        return repository.getAuthData()
    }
}