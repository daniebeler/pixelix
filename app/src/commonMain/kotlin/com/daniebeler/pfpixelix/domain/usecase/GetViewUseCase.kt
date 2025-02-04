package com.daniebeler.pfpixelix.domain.usecase

import com.daniebeler.pfpixelix.domain.repository.StorageRepository
import com.daniebeler.pfpixelix.ui.composables.profile.ViewEnum
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class GetViewUseCase(
    private val repository: StorageRepository
) {
    operator fun invoke(): Flow<ViewEnum>{
        return repository.getStoredView()
    }
}