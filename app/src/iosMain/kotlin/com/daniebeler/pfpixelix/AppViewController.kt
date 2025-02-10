package com.daniebeler.pfpixelix

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.window.ComposeUIViewController
import coil3.SingletonImageLoader
import com.daniebeler.pfpixelix.di.AppComponent
import com.daniebeler.pfpixelix.di.create
import com.daniebeler.pfpixelix.utils.KmpContext
import com.daniebeler.pfpixelix.utils.LocalKmpContext
import platform.UIKit.UIViewController

private object IosContext : KmpContext()

class IosUrlCallback {
    var onRedirect: (String) -> Unit = {}
}

fun AppViewController(urlCallback: IosUrlCallback): UIViewController {
    val appComponent = AppComponent.Companion.create(IosContext)

    SingletonImageLoader.setSafe {
        appComponent.provideImageLoader()
    }

    urlCallback.onRedirect = {
        appComponent.systemUrlHandler.onRedirect(it)
    }

    val finishApp = {}
    return ComposeUIViewController {
        CompositionLocalProvider(
            LocalKmpContext provides IosContext
        ) {
            App(appComponent, finishApp)
        }
    }
}