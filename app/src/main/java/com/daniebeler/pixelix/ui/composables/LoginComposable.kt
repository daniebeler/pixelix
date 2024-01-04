package com.daniebeler.pixelix.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.daniebeler.pixelix.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginComposable(viewModel: LoginViewModel = hiltViewModel()) {

    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    Scaffold { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.full_character),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(32.dp),
            modifier = Modifier
                .padding(paddingValues)
                .padding(12.dp)
        ) {
            /*Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(onClick = {
                    CoroutineScope(Dispatchers.Default).launch {
                        viewModel.login("pixelfed.social", context)
                    }
                }) {
                    Text(text = "Pixelfed.social")
                }
                Button(onClick = {
                    CoroutineScope(Dispatchers.Default).launch {
                        viewModel.login("pixelfed.de", context)
                    }
                }) {
                    Text(text = "Pixelfed.de")
                }
            }*/
            Spacer(modifier = Modifier.weight(1f))
            Row (verticalAlignment = Alignment.Bottom) {
                OutlinedTextField(
                    value = viewModel.customUrl,
                    onValueChange = { viewModel.customUrl = it },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    label = {Text("pixelfed.social")},
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                        disabledContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                        unfocusedBorderColor = Color.Transparent,
                    ),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                            focusManager.clearFocus()
                            //login()
                        }
                    )
                )
                Spacer(Modifier.width(12.dp))
                Button(
                    onClick = {
                        CoroutineScope(Dispatchers.Default).launch {
                            viewModel.login(viewModel.customUrl, context)
                        }
                    },
                    Modifier
                        .height(56.dp)
                        .width(56.dp)
                        .padding(0.dp), shape = RoundedCornerShape(12.dp),
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "submit"
                    )
                }
            }



            if (viewModel.loading) {
                CircularProgressIndicator(
                    modifier = Modifier.width(64.dp),
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            }
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

