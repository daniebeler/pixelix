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
import com.daniebeler.pfpixelix.domain.model.LoginData
import com.daniebeler.pfpixelix.domain.usecase.GetOngoingLoginUseCase
import com.daniebeler.pfpixelix.domain.usecase.ObtainTokenUseCase
import com.daniebeler.pfpixelix.domain.usecase.UpdateLoginDataUseCase
import com.daniebeler.pfpixelix.domain.usecase.VerifyTokenUseCase
import com.daniebeler.pfpixelix.ui.composables.LoginComposable
import com.daniebeler.pfpixelix.ui.theme.PixelixTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : ComponentActivity() {

    @Inject
    lateinit var obtainTokenUseCase: ObtainTokenUseCase

    @Inject
    lateinit var verifyTokenUseCase: VerifyTokenUseCase

    @Inject
    lateinit var updateLoginDataUseCase: UpdateLoginDataUseCase

    @Inject
    lateinit var getOngoingLoginUseCase: GetOngoingLoginUseCase

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
        val loginData: LoginData? = getOngoingLoginUseCase()
        if (loginData == null) {
            error = "an unexpected error occured"
            isLoadingAfterRedirect = false
        } else {
            obtainTokenUseCase(loginData.clientId, loginData.clientSecret, code).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        val newLoginData = loginData.copy(accessToken = result.data!!.accessToken)
                        updateLoginDataUseCase(newLoginData)
                        verifyToken(newLoginData)
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
    }

    private suspend fun verifyToken(loginData: LoginData) {
        verifyTokenUseCase(loginData.accessToken).collect { result ->
            when (result) {
                is Resource.Success -> {
                    val newLoginData = loginData.copy(
                        accountId = result.data!!.id,
                        username = result.data.username,
                        avatar = result.data.avatar,
                        displayName = result.data.displayname,
                        followers = result.data.followersCount,
                        loginOngoing = false
                    )
                    updateLoginDataUseCase(newLoginData, newLoginData.accountId)
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