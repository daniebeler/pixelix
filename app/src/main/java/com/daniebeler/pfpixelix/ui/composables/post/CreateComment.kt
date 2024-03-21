package com.daniebeler.pfpixelix.ui.composables.post

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.daniebeler.pfpixelix.R
import java.util.regex.Pattern

@Composable
fun CreateComment(
    createNewComment: (replyText: String) -> Unit,
    postId: String,
    newReplyState: OwnReplyState,
    viewModel: CreateCommentViewModel = hiltViewModel(key = postId)
) {

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Column {
        Row(verticalAlignment = Alignment.Bottom) {
            OutlinedTextField(
                value = viewModel.replyText,
                onValueChange = {
                    viewModel.changeText(it)
                },
                label = { Text(stringResource(R.string.reply)) },
                modifier = Modifier
                    .weight(1f)
                    .padding(0.dp, 0.dp),
                singleLine = false,
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(onSend = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                    createNewComment(viewModel.replyText.text)
                    viewModel.replyText = viewModel.replyText.copy(text = "")
                    viewModel.mentionsDropdownOpen = false
                })
            )

            Spacer(Modifier.width(12.dp))

            Button(
                onClick = {
                    if (!newReplyState.isLoading) {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                        createNewComment(viewModel.replyText.text)
                        viewModel.replyText = viewModel.replyText.copy(text = "")
                        viewModel.mentionsDropdownOpen = false
                    }
                },
                Modifier
                    .height(56.dp)
                    .width(56.dp)
                    .padding(0.dp, 0.dp),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(12.dp),
                enabled = viewModel.replyText.text.isNotBlank()
            ) {
                if (newReplyState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "submit",
                    Modifier
                        .fillMaxSize()
                        .fillMaxWidth()
                )
                }

            }
        }
        if (viewModel.mentionsDropdownOpen) {
            Box(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .clip(shape = RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                if (viewModel.mentionSuggestions.mentions.isNotEmpty()) {
                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        viewModel.mentionSuggestions.mentions.map {
                            TextButton(onClick = { viewModel.clickMention(it.acct) }) {
                                Text(
                                    text = "@${it.acct}",
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}