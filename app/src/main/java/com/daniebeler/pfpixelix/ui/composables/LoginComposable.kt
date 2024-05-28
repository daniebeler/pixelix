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
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
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
    viewModel: LoginViewModel = hiltViewModel(key = "login-viewmodel-key")
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
                    text = "PIXELIX", fontSize = 38.sp, fontWeight = FontWeight.Black, color = if (isSystemInDarkTheme()) {
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
                    Row(verticalAlignment = Alignment.Bottom) {
                        OutlinedTextField(
                            value = viewModel.customUrl,
                            onValueChange = {
                                viewModel.customUrl = it
                                viewModel.domainChanged()
                            },
                            modifier = Modifier.weight(1f),
                            prefix = { Text("https://") },
                            singleLine = true,
                            label = { Text(stringResource(R.string.server_url)) },
                            shape = RoundedCornerShape(12.dp),
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
                                    .clip(RoundedCornerShape(12.dp))
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
                                shape = RoundedCornerShape(12.dp),
                                contentPadding = PaddingValues(12.dp),
                                enabled = viewModel.isValidUrl,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.onPrimary
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = "submit",
                                    Modifier
                                        .fillMaxSize()
                                        .fillMaxWidth()
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = {
                            val url = "https://pixelfed.org/servers"
                            Navigate.openUrlInApp(context, url)
                        },
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(16.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Box(
                            modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center
                        ) {
                            Row(
                                modifier = Modifier.align(Alignment.CenterStart)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.PersonAdd,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Text(
                                modifier = Modifier.align(Alignment.Center),
                                text = stringResource(id = R.string.i_don_t_have_an_account),
                                color = MaterialTheme.colorScheme.onPrimary,
                                textAlign = TextAlign.Center,
                                fontSize = 15.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(200.dp))
                Spacer(modifier = Modifier.imeAwareInsets(context, 200.dp))
            }
        }
    }
}