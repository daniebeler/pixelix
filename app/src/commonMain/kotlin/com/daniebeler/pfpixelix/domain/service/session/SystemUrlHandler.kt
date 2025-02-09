package com.daniebeler.pfpixelix.domain.service.session

import androidx.compose.ui.platform.UriHandler
import com.daniebeler.pfpixelix.di.AppSingleton
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject

@Inject
@AppSingleton
class SystemUrlHandler {
    private val redirectsFlow = MutableSharedFlow<String>()
    val redirects = redirectsFlow.asSharedFlow()

    var uriHandler: UriHandler? = null

    fun openBrowser(url: String) {
        uriHandler?.openUri(url)
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun onRedirect(url: String) {
        GlobalScope.launch { redirectsFlow.emit(url) }
    }
}