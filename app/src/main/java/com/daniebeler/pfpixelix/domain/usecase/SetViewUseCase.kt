package com.daniebeler.pfpixelix.domain.usecase

import com.daniebeler.pfpixelix.domain.repository.StorageRepository
import com.daniebeler.pfpixelix.ui.composables.profile.ViewEnum

class SetViewUseCase(
    private val repository: StorageRepository
) {
    suspend operator fun invoke(view: ViewEnum) {
        repository.storeView(view)
    }
}