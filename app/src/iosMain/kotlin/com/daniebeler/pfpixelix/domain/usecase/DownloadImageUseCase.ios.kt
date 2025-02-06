package com.daniebeler.pfpixelix.domain.usecase

import com.daniebeler.pfpixelix.utils.KmpContext
import me.tatarka.inject.annotations.Inject

@Inject
actual class DownloadImageUseCase {
    actual operator fun invoke(
        name: String?,
        url: String,
        context: KmpContext
    ) {
    }
}