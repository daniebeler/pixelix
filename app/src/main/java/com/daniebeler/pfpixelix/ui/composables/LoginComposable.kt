package com.daniebeler.pfpixelix.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.rounded.ArrowForwardIos
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daniebeler.pfpixelix.di.injectViewModel
import com.daniebeler.pfpixelix.R
import com.daniebeler.pfpixelix.utils.Navigate
import com.daniebeler.pfpixelix.utils.imeAwareInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun LoginComposable(
    isLoading: Boolean,
    error: String,
    viewModel: LoginViewModel = injectViewModel(key = "login-viewmodel-key") { loginViewModel }
) {
    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(
        color = if (isSystemInDarkTheme()) {
            Color.White
        } else {
            Color.Black
        }, darkIcons = isSystemInDarkTheme()
    )

    systemUiController.setNavigationBarColor(
        color = Color.Transparent, darkIcons = isSystemInDarkTheme()
    )
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        if (isSystemInDarkTheme()) {
                            Color.White
                        } else {
                            Color.Black
                        }
                    ), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(50.dp))
                Image(
                    painterResource(
                        if (isSystemInDarkTheme()) {
                            R.drawable.pixelix_logo_black_xxl
                        } else {
                            R.drawable.pixelix_logo_white_xxl
                        }
                    ), contentDescription = null,
                    Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "PIXELIX",
                    fontSize = 38.sp,
                    fontWeight = FontWeight.Black,
                    color = if (isSystemInDarkTheme()) {
                        Color.Black
                    } else {
                        Color.White
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))
            }

            Image(
                painterResource(
                    id = if (isSystemInDarkTheme()) {
                        R.drawable.login_wave_light
                    } else {
                        R.drawable.login_wave_dark
                    }
                ),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.fillMaxWidth()
            )

            if (isLoading) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    CircularProgressIndicator()
                }
            } else if (error.isNotBlank()) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    Text(text = error)
                }
            } else {
                Spacer(modifier = Modifier.weight(1f))
                Column(Modifier.padding(12.dp)) {

                    Row {
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = stringResource(R.string.server_url), fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(Modifier.height(6.dp))

                    Row(verticalAlignment = Alignment.Bottom) {


                        TextField(
                            value = viewModel.customUrl,
                            prefix = { Text("https://") },
                            singleLine = true,
                            onValueChange = {
                                viewModel.customUrl = it
                                viewModel.domainChanged()
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(16.dp),
                            colors = TextFieldDefaults.colors(
                                unfocusedIndicatorColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer
                            ),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(onDone = {
                                keyboardController?.hide()
                                focusManager.clearFocus()
                                CoroutineScope(Dispatchers.Default).launch {
                                    viewModel.login(viewModel.customUrl, context)
                                }
                            })
                        )

                        Spacer(Modifier.width(12.dp))
                        if (viewModel.loading) {
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
                                onClick = {
                                    CoroutineScope(Dispatchers.Default).launch {
                                        viewModel.login(viewModel.customUrl, context)
                                    }
                                },
                                Modifier
                                    .height(56.dp)
                                    .width(56.dp)
                                    .padding(0.dp, 0.dp),
                                shape = RoundedCornerShape(16.dp),
                                contentPadding = PaddingValues(12.dp),
                                enabled = viewModel.isValidUrl,
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

                    TextButton(onClick = {
                        val url = "https://pixelfed.org/servers"
                        Navigate.openUrlInApp(context, url)
                    }) {
                        Text(
                            stringResource(id = R.string.i_don_t_have_an_account),
                            textDecoration = TextDecoration.Underline,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                }

                Spacer(modifier = Modifier.height(200.dp))
                Spacer(modifier = Modifier.imeAwareInsets(context, 200.dp))
            }
        }
    }
}