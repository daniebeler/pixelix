package com.daniebeler.pfpixelix.domain.usecase

import com.daniebeler.pfpixelix.domain.repository.StorageRepository
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class GetUseInAppBrowserUseCase(
    private val repository: StorageRepository
) {
    operator fun invoke(): Flow<Boolean> {
        return repository.getUseInAppBrowser()
    }
}