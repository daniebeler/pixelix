package com.daniebeler.pfpixelix.ui.composables.session

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForwardIos
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.daniebeler.pfpixelix.common.Destinations
import com.daniebeler.pfpixelix.di.injectViewModel
import com.daniebeler.pfpixelix.utils.LocalKmpContext
import com.daniebeler.pfpixelix.utils.Navigate
import com.daniebeler.pfpixelix.utils.imeAwareInsets
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pixelix.app.generated.resources.Res
import pixelix.app.generated.resources.i_don_t_have_an_account
import pixelix.app.generated.resources.login_wave_dark
import pixelix.app.generated.resources.login_wave_light
import pixelix.app.generated.resources.pixelix_logo_black_xxl
import pixelix.app.generated.resources.pixelix_logo_white_xxl
import pixelix.app.generated.resources.server_url

@Composable
fun LoginComposable(
    viewModel: LoginViewModel = injectViewModel("LoginViewModel") { loginViewModel }
) {
    Scaffold(Modifier.fillMaxSize()) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        if (isSystemInDarkTheme()) Color.White else Color.Black
                    )
                    .windowInsetsPadding(
                        WindowInsets.systemBars.only(
                            WindowInsetsSides.Top + WindowInsetsSides.Horizontal
                        )
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(50.dp))
                Image(
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape),
                    painter = painterResource(
                        if (isSystemInDarkTheme()) {
                            Res.drawable.pixelix_logo_black_xxl
                        } else {
                            Res.drawable.pixelix_logo_white_xxl
                        }
                    ),
                    contentDescription = null
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "PIXELIX",
                    fontSize = 38.sp,
                    fontWeight = FontWeight.Black,
                    color = if (isSystemInDarkTheme()) Color.Black else Color.White
                )

                Spacer(modifier = Modifier.height(24.dp))
            }

            Image(
                painterResource(
                    if (isSystemInDarkTheme()) {
                        Res.drawable.login_wave_light
                    } else {
                        Res.drawable.login_wave_dark
                    }
                ),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.fillMaxWidth()
            )

            viewModel.error?.let { err ->
                if (err.isNotBlank()) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                        Text(text = err)
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))
            Column(Modifier.padding(12.dp)) {

                Row {
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = stringResource(Res.string.server_url),
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(Modifier.height(6.dp))

                val keyboardController = LocalSoftwareKeyboardController.current
                val focusManager = LocalFocusManager.current
                fun login() {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                    viewModel.auth()
                }

                Row(verticalAlignment = Alignment.Bottom) {
                    TextField(
                        value = viewModel.serverHost,
                        onValueChange = { viewModel.updateServerHost(it) },
                        prefix = { Text("https://") },
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        colors = TextFieldDefaults.colors(
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer
                        ),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { login() })
                    )

                    Spacer(Modifier.width(12.dp))
                    if (viewModel.isLoading) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .height(56.dp)
                                .width(56.dp)
                                .padding(0.dp, 0.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(MaterialTheme.colorScheme.primary)

                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    } else {
                        Button(
                            onClick = { login() },
                            Modifier
                                .height(56.dp)
                                .width(56.dp)
                                .padding(0.dp, 0.dp),
                            shape = RoundedCornerShape(16.dp),
                            contentPadding = PaddingValues(12.dp),
                            enabled = viewModel.isValidHost,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary,
                                disabledContainerColor = MaterialTheme.colorScheme.surfaceContainer
                            )
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.ArrowForwardIos,
                                contentDescription = "submit",
                                Modifier
                                    .fillMaxSize()
                                    .fillMaxWidth()
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))

                val context = LocalKmpContext.current
                TextButton(onClick = {
                    val url = "https://pixelfed.org/servers"
                    Navigate.openUrlInApp(context, url)
                }) {
                    Text(
                        stringResource(Res.string.i_don_t_have_an_account),
                        textDecoration = TextDecoration.Underline,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

            }

            Spacer(modifier = Modifier.height(200.dp))
            Spacer(modifier = Modifier.imeAwareInsets(200.dp))
        }
    }
}