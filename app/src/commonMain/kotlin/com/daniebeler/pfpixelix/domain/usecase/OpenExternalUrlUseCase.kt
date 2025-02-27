package com.daniebeler.pfpixelix.domain.usecase

import com.daniebeler.pfpixelix.domain.service.preferences.UserPreferences
import com.daniebeler.pfpixelix.utils.KmpContext
import me.tatarka.inject.annotations.Inject

@Inject
expect class OpenExternalUrlUseCase(
    prefs: UserPreferences
) {
    operator fun invoke(url: String, context: KmpContext)
}