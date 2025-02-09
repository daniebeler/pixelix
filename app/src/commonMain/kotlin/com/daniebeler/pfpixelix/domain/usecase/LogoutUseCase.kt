package com.daniebeler.pfpixelix.domain.usecase

import com.daniebeler.pfpixelix.domain.repository.SavedSearchesRepository
import com.daniebeler.pfpixelix.domain.service.session.AuthService
import me.tatarka.inject.annotations.Inject

@Inject
class LogoutUseCase(
    private val authService: AuthService,
    private val savedSearchesRepository: SavedSearchesRepository
) {
    suspend operator fun invoke() {
        authService.deleteSession()
        savedSearchesRepository.clearSavedSearches()
    }
}