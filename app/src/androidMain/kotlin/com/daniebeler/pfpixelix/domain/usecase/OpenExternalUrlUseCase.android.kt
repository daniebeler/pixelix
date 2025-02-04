package com.daniebeler.pfpixelix.domain.usecase

import com.daniebeler.pfpixelix.domain.repository.StorageRepository
import com.daniebeler.pfpixelix.utils.KmpContext
import com.daniebeler.pfpixelix.utils.Navigate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject

@Inject
actual class OpenExternalUrlUseCase actual constructor(
    private val repository: StorageRepository
) {
    actual operator fun invoke(url: String, context: KmpContext) {
        CoroutineScope(Dispatchers.Default).launch {
            val useInAppBrowser = repository.getUseInAppBrowser().first()
            if (useInAppBrowser) {
                Navigate.openUrlInApp(context, url)
            } else {
                Navigate.openUrlInBrowser(context, url)
            }
        }
    }
}