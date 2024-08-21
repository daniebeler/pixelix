package com.daniebeler.pfpixelix.ui.composables.newpost

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.ArrowDownward
import androidx.compose.material.icons.outlined.ArrowUpward
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.decode.VideoFrameDecoder
import coil.request.ImageRequest
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.daniebeler.pfpixelix.R
import com.daniebeler.pfpixelix.common.Constants.AUDIENCE_FOLLOWERS_ONLY
import com.daniebeler.pfpixelix.common.Constants.AUDIENCE_PUBLIC
import com.daniebeler.pfpixelix.common.Constants.AUDIENCE_UNLISTED
import com.daniebeler.pfpixelix.ui.composables.states.ErrorComposable
import com.daniebeler.pfpixelix.ui.composables.states.LoadingComposable
import com.daniebeler.pfpixelix.ui.composables.textfield_mentions.TextFieldMentionsComposable
import com.daniebeler.pfpixelix.utils.MimeType
import com.daniebeler.pfpixelix.utils.Navigate
import com.daniebeler.pfpixelix.utils.imeAwareInsets

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun NewPostComposable(
    navController: NavController,
    viewModel: NewPostViewModel = hiltViewModel(key = "new-post-viewmodel-key")
) {
    val context = LocalContext.current

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(),
        onResult = { uris ->
            Navigate.navigate("new_post_screen", navController)
            uris.forEach {
                viewModel.addImage(it, context)
            }
        })

    var expanded by remember { mutableStateOf(false) }
    var showReleaseAlert by remember {
        mutableStateOf(false)
    }

    Scaffold(contentWindowInsets = WindowInsets(0.dp), topBar = {
        TopAppBar(windowInsets = WindowInsets(0, 0, 0, 0), title = {
            Text(text = stringResource(R.string.new_post), fontWeight = FontWeight.Bold)
        }, navigationIcon = {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = ""
                )
            }
        }, actions = {
            Button(
                onClick = { showReleaseAlert = true },
                enabled = (viewModel.images.isNotEmpty() && !viewModel.mediaUploadState.isLoading)
            ) {
                Text(text = stringResource(R.string.release))
            }
        })
    }) { paddingValues ->
        Box {
            Column(
                Modifier
                    .padding(paddingValues)
                    .imeAwareInsets(context, 100.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(12.dp), verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                viewModel.images.forEachIndexed { index, image ->
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Box(contentAlignment = Alignment.Center) {

                            val type = MimeType.getMimeType(image.imageUri, context.contentResolver)
                            if (type != null && type.take(5) == "video") {
                                val model = ImageRequest.Builder(context).data(image.imageUri)
                                    .decoderFactory { result, options, _ ->
                                        VideoFrameDecoder(
                                            result.source, options
                                        )
                                    }.build()

                                AsyncImage(
                                    model = model,
                                    contentDescription = "video thumbnail",
                                    modifier = Modifier.width(100.dp)
                                )
                            } else if (type != null && type.takeLast(3) == "gif") {
                                GlideImage(
                                    model = image.imageUri,
                                    contentDescription = null,
                                    modifier = Modifier.width(100.dp)
                                )
                            } else {
                                AsyncImage(
                                    model = image.imageUri,
                                    contentDescription = null,
                                    modifier = Modifier.width(100.dp)
                                )
                            }
                            if (image.isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.wrapContentSize(Alignment.Center)
                                )
                            }
                        }
                        Spacer(Modifier.width(10.dp))

                        OutlinedTextField(
                            value = image.text,
                            onValueChange = { viewModel.updateAltTextVariable(index, it) },
                            modifier = Modifier.weight(1f),
                            label = { Text(stringResource(R.string.alt_text)) },
                        )

                        if (viewModel.images.size > 1) {
                            Column {
                                IconButton(onClick = { viewModel.moveMediaAttachmentUp(index) }) {
                                    Icon(
                                        imageVector = Icons.Outlined.ArrowUpward,
                                        contentDescription = "move Imageupwards"
                                    )
                                }
                                IconButton(onClick = { viewModel.moveMediaAttachmentDown(index) }) {
                                    Icon(
                                        imageVector = Icons.Outlined.ArrowDownward,
                                        contentDescription = "move Imageupwards"
                                    )
                                }
                            }
                        }
                        IconButton(onClick = {
                            viewModel.deleteMedia(image.id, image.imageUri)
                        }) {
                            Icon(
                                imageVector = Icons.Outlined.Delete,
                                contentDescription = "delete Image",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }

                    }

                }
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Icon(
                        modifier = Modifier
                            .clickable {
                                singlePhotoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo)
                                )
                            }
                            .height(50.dp)
                            .width(50.dp),
                        imageVector = Icons.Filled.Add,
                        contentDescription = null,
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                TextFieldMentionsComposable(
                    submit = {},
                    text = viewModel.caption,
                    changeText = { text -> viewModel.caption = text },
                    labelStringId = R.string.caption,
                    modifier = Modifier.fillMaxWidth(),
                    imeAction = ImeAction.Default,
                    suggestionsBoxColor = MaterialTheme.colorScheme.surfaceContainer,
                    submitButton = null
                )/*OutlinedTextField(
                    value = viewModel.caption,
                    onValueChange = { viewModel.caption = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(R.string.caption)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                        disabledContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                        unfocusedBorderColor = Color.Transparent,
                    ),
                    shape = RoundedCornerShape(12.dp),
                )*/
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = stringResource(R.string.sensitive_nsfw_media))
                    Switch(checked = viewModel.sensitive,
                        onCheckedChange = { viewModel.sensitive = it })
                }
                if (viewModel.sensitive) {
                    OutlinedTextField(
                        value = viewModel.sensitiveText,
                        onValueChange = { viewModel.sensitiveText = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text(stringResource(R.string.content_warning_or_spoiler_text)) },
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = stringResource(R.string.audience))
                    Box {
                        OutlinedButton(onClick = { expanded = !expanded }) {
                            val buttonText: String = when (viewModel.audience) {
                                AUDIENCE_PUBLIC -> {
                                    stringResource(id = R.string.audience_public)
                                }

                                AUDIENCE_UNLISTED -> {
                                    stringResource(id = R.string.unlisted)
                                }

                                AUDIENCE_FOLLOWERS_ONLY -> {
                                    stringResource(id = R.string.followers_only)
                                }

                                else -> {
                                    ""
                                }
                            }
                            Text(text = buttonText)
                        }
                        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                            DropdownMenuItem(text = { Text(stringResource(R.string.audience_public)) },
                                onClick = { viewModel.audience = AUDIENCE_PUBLIC },
                                trailingIcon = {
                                    if (viewModel.audience == AUDIENCE_PUBLIC) {
                                        Icon(
                                            imageVector = Icons.Outlined.Check,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                })
                            DropdownMenuItem(text = { Text(stringResource(R.string.unlisted)) },
                                onClick = { viewModel.audience = AUDIENCE_UNLISTED },
                                trailingIcon = {
                                    if (viewModel.audience == AUDIENCE_UNLISTED) {
                                        Icon(
                                            imageVector = Icons.Outlined.Check,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                })
                            DropdownMenuItem(text = { Text(stringResource(R.string.followers_only)) },
                                onClick = { viewModel.audience = AUDIENCE_FOLLOWERS_ONLY },
                                trailingIcon = {
                                    if (viewModel.audience == AUDIENCE_FOLLOWERS_ONLY) {
                                        Icon(
                                            imageVector = Icons.Outlined.Check,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                })
                        }
                    }
                }
            }

            if (viewModel.addImageError.first.isNotBlank()) {
                AlertDialog(title = {
                    Text(text = viewModel.addImageError.first)
                }, text = {
                    Text(text = viewModel.addImageError.second)
                }, onDismissRequest = {
                    viewModel.addImageError = Pair("", "")
                }, confirmButton = {
                    TextButton(onClick = {
                        viewModel.addImageError = Pair("", "")
                    }) {
                        Text("Ok")
                    }
                })
            }

            if (showReleaseAlert) {
                AlertDialog(title = {
                    Text(text = "Are you sure?")
                }, onDismissRequest = {
                    showReleaseAlert = false
                }, dismissButton = {
                    TextButton(onClick = {
                        showReleaseAlert = false
                    }) {
                        Text(stringResource(id = R.string.cancel))
                    }
                }, confirmButton = {
                    TextButton(onClick = {
                        showReleaseAlert = false
                        viewModel.post(navController)
                    }) {
                        Text(stringResource(id = R.string.release))
                    }
                })
            }

            LoadingComposable(isLoading = viewModel.createPostState.isLoading)
            //LoadingComposable(isLoading = viewModel.mediaUploadState.isLoading)
            ErrorComposable(message = viewModel.mediaUploadState.error)
            ErrorComposable(message = viewModel.createPostState.error)
        }
    }
}