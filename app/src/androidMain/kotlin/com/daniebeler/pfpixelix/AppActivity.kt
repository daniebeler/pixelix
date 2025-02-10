package com.daniebeler.pfpixelix

import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import co.touchlab.kermit.Logger
import com.daniebeler.pfpixelix.utils.LocalKmpContext
import com.daniebeler.pfpixelix.utils.Navigate
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class AppActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CompositionLocalProvider(
                LocalKmpContext provides this
            ) {
                App(MyApplication.appComponent) { finish() }
            }
        }
        if (savedInstanceState == null) {
            handleNewIntent(intent)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleNewIntent(intent)
    }

    private fun handleNewIntent(intent: Intent) {
        when (intent.action) {
            Intent.ACTION_VIEW -> {
                intent.dataString?.let { onExternalUrl(it) }
            }
            Intent.ACTION_SEND, Intent.ACTION_SEND_MULTIPLE -> {
                val imageUris = handleSharePhotoIntent(intent, contentResolver, cacheDir)
                if (imageUris.isNotEmpty()) {
                    imageUris.forEach { uri ->
                        try {
                            contentResolver.takePersistableUriPermission(
                                uri, Intent.FLAG_GRANT_READ_URI_PERMISSION
                            )
                        } catch (e: SecurityException) {
                            e.printStackTrace() // Handle permission denial gracefully
                        }
                    }
                    onExternalFileShare(imageUris)
                }
            }
        }
    }

    private fun onExternalUrl(url: String) {
        val systemUrlHandler = MyApplication.appComponent.systemUrlHandler
        systemUrlHandler.onRedirect(url)
    }

    private fun onExternalFileShare(uris: List<Uri>) {
        val systemFileShare = MyApplication.appComponent.systemFileShare
        systemFileShare.share(uris)
    }
}

@Composable
actual fun EdgeToEdgeDialog(
    onDismissRequest: () -> Unit,
    properties: DialogProperties,
    content: @Composable () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnBackPress = properties.dismissOnBackPress,
            dismissOnClickOutside = properties.dismissOnClickOutside,
            usePlatformDefaultWidth = true,
            decorFitsSystemWindows = false
        ),
        content = {
            SetUpEdgeToEdgeDialog()
            content()
        }
    )
}

@Composable
private fun SetUpEdgeToEdgeDialog() {
    val parentView = LocalView.current.parent as View
    val window = (parentView as DialogWindowProvider).window

    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

    window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        window.attributes.fitInsetsTypes = 0
        window.attributes.fitInsetsSides = 0
    }
}

private fun saveUriToCache(uri: Uri, contentResolver: ContentResolver, cacheDir: File): Uri? {
    try {
        val inputStream: InputStream? = contentResolver.openInputStream(uri)
        inputStream?.use { input ->
            val file = File(cacheDir, "shared_image_${System.currentTimeMillis()}.jpg")
            FileOutputStream(file).use { output ->
                input.copyTo(output)
            }
            return Uri.fromFile(file) // Return the new cached URI
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}

private fun handleSharePhotoIntent(
    intent: Intent, contentResolver: ContentResolver, cacheDir: File
): List<Uri> {
    val action = intent.action
    val type = intent.type
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

    var imageUris: List<Uri> = emptyList()
    when {
        Intent.ACTION_SEND == action && type != null -> {
            if (type.startsWith("image/") || type.startsWith("video/")) {
                val singleUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    intent.getParcelableExtra(
                        Intent.EXTRA_STREAM, Uri::class.java
                    )
                } else {
                    @Suppress("DEPRECATION") intent.getParcelableExtra(
                        Intent.EXTRA_STREAM
                    ) as? Uri
                }
                singleUri?.let { uri ->
                    val cachedUri = saveUriToCache(uri, contentResolver, cacheDir)
                    imageUris =
                        cachedUri?.let { listOf(it) } ?: emptyList() // Wrap single image in a list
                }
            }
        }

        Intent.ACTION_SEND_MULTIPLE == action && type != null -> {
            val receivedUris = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableArrayListExtra(
                    Intent.EXTRA_STREAM, Uri::class.java
                )
            } else {
                @Suppress("DEPRECATION") intent.getParcelableArrayListExtra(
                    Intent.EXTRA_STREAM
                )
            }
            imageUris = receivedUris?.mapNotNull {
                saveUriToCache(
                    it, contentResolver, cacheDir
                )
            } ?: emptyList()
        }
    }
    return imageUris
}