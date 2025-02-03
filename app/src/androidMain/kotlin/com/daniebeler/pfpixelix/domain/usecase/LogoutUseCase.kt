package com.daniebeler.pfpixelix.domain.usecase

import com.daniebeler.pfpixelix.domain.repository.AuthRepository
import com.daniebeler.pfpixelix.domain.repository.SavedSearchesRepository
import me.tatarka.inject.annotations.Inject

@Inject
class LogoutUseCase(
    private val authRepository: AuthRepository,
    private val savedSearchesRepository: SavedSearchesRepository
) {
    suspend operator fun invoke() {
        authRepository.logout()
        savedSearchesRepository.clearSavedSearches()
    }
}