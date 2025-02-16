package com.daniebeler.pfpixelix

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.window.ComposeUIViewController
import coil3.SingletonImageLoader
import com.daniebeler.pfpixelix.di.AppComponent
import com.daniebeler.pfpixelix.di.create
import com.daniebeler.pfpixelix.utils.KmpContext
import com.daniebeler.pfpixelix.utils.LocalKmpContext
import platform.UIKit.UIViewController

class IosUrlCallback {
    var onRedirect: (String) -> Unit = {}
}

fun AppViewController(urlCallback: IosUrlCallback): UIViewController {
    var viewController: UIViewController? = null
    val context = object : KmpContext() {
        override val viewController: UIViewController
            get() = viewController!!
    }
    val appComponent = AppComponent.Companion.create(context)

    SingletonImageLoader.setSafe {
        appComponent.provideImageLoader()
    }

    urlCallback.onRedirect = {
        appComponent.systemUrlHandler.onRedirect(it)
    }

    val finishApp = {}
    viewController = ComposeUIViewController {
        CompositionLocalProvider(
            LocalKmpContext provides context
        ) {
            App(appComponent, finishApp)
        }
    }

    return viewController
}