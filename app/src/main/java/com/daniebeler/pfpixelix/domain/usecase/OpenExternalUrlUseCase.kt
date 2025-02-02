package com.daniebeler.pfpixelix.domain.usecase

import android.content.Context
import com.daniebeler.pfpixelix.ui.composables.settings.preferences.prefs.UseInAppBrowserPrefUtil
import com.daniebeler.pfpixelix.utils.Navigate
import dagger.hilt.android.qualifiers.ApplicationContext

class OpenExternalUrlUseCase(@ApplicationContext private val context: Context) {
    operator fun invoke(url: String) {
        val useInAppBrowser = UseInAppBrowserPrefUtil.isEnable(context)
        if (useInAppBrowser) {
            Navigate.openUrlInApp(context, url)
        } else {
            Navigate.openUrlInBrowser(context, url)
        }
    }
}