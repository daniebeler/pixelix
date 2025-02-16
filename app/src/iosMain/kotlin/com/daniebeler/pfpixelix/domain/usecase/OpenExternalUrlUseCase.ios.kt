package com.daniebeler.pfpixelix.domain.usecase

import com.daniebeler.pfpixelix.domain.repository.StorageRepository
import com.daniebeler.pfpixelix.utils.KmpContext
import com.daniebeler.pfpixelix.utils.openUrlInBrowser
import me.tatarka.inject.annotations.Inject

@Inject
actual class OpenExternalUrlUseCase actual constructor(repository: StorageRepository) {
    actual operator fun invoke(url: String, context: KmpContext) {
        context.openUrlInBrowser(url)
    }
}