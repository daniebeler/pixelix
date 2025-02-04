package com.daniebeler.pfpixelix.domain.usecase

import com.daniebeler.pfpixelix.utils.KmpContext
import me.tatarka.inject.annotations.Inject

@Inject
expect class DisableAllCustomAppIconsUseCase() {
    operator fun invoke(context: KmpContext)
}