package com.daniebeler.pfpixelix

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.usecase.GetClientIdUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetClientSecretUseCase
import com.daniebeler.pfpixelix.domain.usecase.ObtainTokenUseCase
import com.daniebeler.pfpixelix.domain.usecase.StoreAccessTokenUseCase
import com.daniebeler.pfpixelix.domain.usecase.StoreAccountIdUseCase
import com.daniebeler.pfpixelix.domain.usecase.VerifyTokenUseCase
import com.daniebeler.pfpixelix.ui.composables.LoginComposable
import com.daniebeler.pfpixelix.ui.theme.PixelixTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : ComponentActivity() {

    @Inject
    lateinit var obtainTokenUseCase: ObtainTokenUseCase

    @Inject
    lateinit var getClientIdUseCase: GetClientIdUseCase

    @Inject
    lateinit var getClientSecretUseCase: GetClientSecretUseCase

    @Inject
    lateinit var storeAccessTokenUseCase: StoreAccessTokenUseCase

    @Inject
    lateinit var storeAccountIdUseCase: StoreAccountIdUseCase

    @Inject
    lateinit var verifyTokenUseCase: VerifyTokenUseCase

    private var isLoadingAfterRedirect: Boolean by mutableStateOf(false)
    private var error: String by mutableStateOf("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            PixelixTheme {
                Scaffold { paddingValues ->
                    Column(Modifier.padding(paddingValues)) {

                    }
                    LoginComposable(isLoadingAfterRedirect, error)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val url: Uri? = intent.data

        //Check if the activity was started after the authentication
        if (url == null || !url.toString().startsWith("pixelix-android-auth://callback")) return

        val code = url.getQueryParameter("code") ?: ""


        if (code.isNotEmpty()) {

            isLoadingAfterRedirect = true
            CoroutineScope(Dispatchers.Default).launch {
                getTokenAndRedirect(code)
            }
        }
    }

    private suspend fun getTokenAndRedirect(code: String) {

        val clientId: String = getClientIdUseCase().first()
        val clientSecret: String = getClientSecretUseCase().first()


        obtainTokenUseCase(clientId, clientSecret, code).collect { result ->
            when (result) {
                is Resource.Success -> {
                    storeAccessTokenUseCase(result.data!!.accessToken)
                    verifyToken(result.data.accessToken)
                }

                is Resource.Error -> {
                    error = result.message ?: "Error"
                    isLoadingAfterRedirect = false
                }

                is Resource.Loading -> {
                    isLoadingAfterRedirect = true
                }
            }
        }
    }

    private suspend fun verifyToken(token: String) {
        verifyTokenUseCase(token).collect { result ->
            when (result) {
                is Resource.Success -> {
                    storeAccountIdUseCase(result.data!!.id)
                    redirect()
                }

                is Resource.Error -> {
                    error = result.message ?: "Error"
                    isLoadingAfterRedirect = false
                }

                is Resource.Loading -> {
                    isLoadingAfterRedirect = true
                }
            }
        }
    }

    private fun redirect() {
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        applicationContext.startActivity(intent)
    }
}