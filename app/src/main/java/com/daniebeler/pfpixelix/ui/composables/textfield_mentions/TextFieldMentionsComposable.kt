package com.daniebeler.pfpixelix.ui.composables.textfield_mentions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.daniebeler.pfpixelix.R

@Composable
fun TextFieldMentionsComposable(
    submit: (text: String) -> Unit,
    text: TextFieldValue,
    changeText: (newText: TextFieldValue) -> Unit,
    labelStringId: Int,
    submitButton: (@Composable () -> Unit)?,
    modifier: Modifier?,
    imeAction: ImeAction,
    viewModel: TextFieldMentionsViewModel = hiltViewModel()
) {

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = text,
                onValueChange = {
                    viewModel.changeText(it)
                    changeText(it)
                },
                label = { Text(stringResource(labelStringId)) },
                modifier = modifier ?: Modifier,
                singleLine = false,
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(imeAction = imeAction),
                keyboardActions = KeyboardActions(onSend = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                    submit(text.text)
                    viewModel.mentionsDropdownOpen = false
                })
            )
            if (submitButton != null) {
                Spacer(modifier = Modifier.width(12.dp))
                submitButton()
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
                            TextButton(onClick = {
                                viewModel.mentionsDropdownOpen = false
                                val index = text.text.lastIndexOf("@") + 1
                                val newText = text.text.substring(0, index) + it.acct
                                changeText(
                                    text.copy(
                                        text = newText, selection = TextRange(newText.length)
                                    )
                                )
                            }) {
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