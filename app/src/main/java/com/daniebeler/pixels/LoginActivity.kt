package com.daniebeler.pixels

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.daniebeler.pixels.ui.composables.LoginComposable
import com.daniebeler.pixels.ui.theme.PixelsTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            PixelsTheme {
                Scaffold { paddingValues ->
                    Column (Modifier.padding(paddingValues)) {

                    }
                    LoginComposable(viewModel = mainViewModel)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val url: Uri? = intent.data

        //Check if the activity was started after the authentication
        if (url == null || !url.toString().startsWith("pixels-android-auth://callback")) return

        val code = url.getQueryParameter("code") ?:""


        if (code.isNotEmpty()) {
            CoroutineScope(Dispatchers.Default).launch {
                if (mainViewModel.obtainToken(code)) {
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    applicationContext.startActivity(intent)
                }
            }
        }

    }
}