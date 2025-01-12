package com.daniebeler.pfpixelix.ui.composables.textfield_location

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.daniebeler.pfpixelix.domain.model.Place

@Composable
fun TextFieldLocationsComposable(
    submit: (id: String) -> Unit,
    submitPlace: (place: Place?) -> Unit,
    initialValue: Place?,
    labelStringId: Int,
    submitButton: (@Composable () -> Unit)?,
    modifier: Modifier?,
    imeAction: ImeAction,
    suggestionsBoxColor: Color,
    viewModel: TextFieldLocationsViewModel = hiltViewModel()
) {

    LaunchedEffect(initialValue) {
        initialValue?.let {
            viewModel.initializePlace(initialValue)
        }
    }

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    Column {

        if (viewModel.locationsSuggestions.location != null) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(
                        MaterialTheme.colorScheme.surfaceContainerHighest,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .height(56.dp).padding(horizontal = 12.dp), verticalAlignment = Alignment.CenterVertically
            ) {
                Text("${viewModel.locationsSuggestions.location!!.name!!}, ${viewModel.locationsSuggestions.location!!.country}")
                Spacer(Modifier.weight(1f))
                IconButton(onClick = {
                    viewModel.edit()
                    submit("")
                    submitPlace(null)
                }) {
                    Icon(imageVector = Icons.Outlined.Edit, contentDescription = "edit")
                }
                IconButton(onClick = {
                    viewModel.removeLocation()
                    submit("")
                    submitPlace(null)
                }) {
                    Icon(imageVector = Icons.Outlined.Delete, contentDescription = "remove")
                }
            }
        } else {
            Row(verticalAlignment = Alignment.CenterVertically) {

                TextField(
                    value = viewModel.text,
                    singleLine = false,
                    onValueChange = {
                        viewModel.changeText(it)
                    },
                    placeholder = { Text(stringResource(labelStringId)) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = TextFieldDefaults.colors(
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = imeAction),
                    keyboardActions = KeyboardActions(onDone = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    })
                )


                if (submitButton != null) {
                    Spacer(modifier = Modifier.width(12.dp))
                    submitButton()
                }
            }
        }
        if (viewModel.locationsDropdownOpen) {
            Box(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .clip(shape = RoundedCornerShape(12.dp))
                    .background(suggestionsBoxColor)
                    .fillMaxWidth()
            ) {
                if (viewModel.locationsSuggestions.locations.isNotEmpty()) {
                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        viewModel.locationsSuggestions.locations.map {
                            TextButton(onClick = {
                                viewModel.clickLocation(it)
                                submit(it.id)
                                submitPlace(it)
                            }) {
                                Text(
                                    text = "${it.name ?: ""}, ${it.country}",
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