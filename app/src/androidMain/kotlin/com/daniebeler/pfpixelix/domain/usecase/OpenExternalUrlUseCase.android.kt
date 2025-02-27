package com.daniebeler.pfpixelix.domain.usecase

import com.daniebeler.pfpixelix.domain.service.preferences.UserPreferences
import com.daniebeler.pfpixelix.utils.KmpContext
import com.daniebeler.pfpixelix.utils.Navigate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject

@Inject
actual class OpenExternalUrlUseCase actual constructor(
    private val prefs: UserPreferences
) {
    actual operator fun invoke(url: String, context: KmpContext) {
        CoroutineScope(Dispatchers.Default).launch {
            if (prefs.useInAppBrowser) {
                Navigate.openUrlInApp(context, url)
            } else {
                Navigate.openUrlInBrowser(context, url)
            }
        }
    }
}