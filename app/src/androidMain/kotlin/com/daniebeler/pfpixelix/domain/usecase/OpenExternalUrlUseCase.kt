package com.daniebeler.pfpixelix.domain.usecase

import android.content.Context
import com.daniebeler.pfpixelix.ui.composables.settings.preferences.prefs.UseInAppBrowserPrefUtil
import com.daniebeler.pfpixelix.utils.Navigate
import me.tatarka.inject.annotations.Inject

@Inject
class OpenExternalUrlUseCase {
    operator fun invoke(url: String, context: Context) {
        val useInAppBrowser = UseInAppBrowserPrefUtil.isEnable(context)
        if (useInAppBrowser) {
            Navigate.openUrlInApp(context, url)
        } else {
            Navigate.openUrlInBrowser(context, url)
        }
    }
}