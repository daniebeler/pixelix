package com.daniebeler.pfpixelix.domain.usecase

import com.daniebeler.pfpixelix.domain.repository.StorageRepository
import com.daniebeler.pfpixelix.utils.KmpContext
import me.tatarka.inject.annotations.Inject

@Inject
expect class OpenExternalUrlUseCase(
    repository: StorageRepository
) {
    operator fun invoke(url: String, context: KmpContext)
}