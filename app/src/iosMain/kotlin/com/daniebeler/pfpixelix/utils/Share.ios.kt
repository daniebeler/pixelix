package com.daniebeler.pfpixelix.utils

import platform.UIKit.UIActivityViewController

actual object Share {
    actual fun shareText(context: KmpContext, text: String) {
        val vc = UIActivityViewController(listOf(text), null)
        context.viewController.presentViewController(vc, true, null)
    }
}