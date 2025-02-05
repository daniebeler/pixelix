package com.daniebeler.pfpixelix.ui.composables.edit_post

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.ArrowDownward
import androidx.compose.material.icons.outlined.ArrowUpward
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.video.VideoFrameDecoder
import pixelix.app.generated.resources.Res
import pixelix.app.generated.resources.*
import com.daniebeler.pfpixelix.di.injectViewModel
import com.daniebeler.pfpixelix.ui.composables.states.ErrorComposable
import com.daniebeler.pfpixelix.ui.composables.states.LoadingComposable
import com.daniebeler.pfpixelix.ui.composables.textfield_location.TextFieldLocationsComposable
import com.daniebeler.pfpixelix.ui.composables.textfield_mentions.TextFieldMentionsComposable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPostComposable(
    postId: String, navController: NavController, viewModel: EditPostViewModel = injectViewModel("editPostViewModel") { editPostViewModel }
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    var showSaveAlert by remember {
        mutableStateOf(false)
    }

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.loadData(postId)
    }

    Scaffold(contentWindowInsets = WindowInsets.systemBars.only(WindowInsetsSides.Top),
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(scrollBehavior = scrollBehavior,
                title = {
                    Text(
                        text = stringResource(Res.string.edit_post), fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = ""
                        )
                    }
                },
                actions = {
                    if (viewModel.editPostState.post != null) {
                        val mediaAttachmentsEditIds = viewModel.mediaAttachmentsEdit.map { it.id }
                        val mediaAttachmentsBeforeIds =
                            viewModel.mediaAttachmentsBefore.map { it.id }
                        if (viewModel.mediaDescriptionItems.any { it.changed } || viewModel.caption.text != viewModel.editPostState.post!!.content || viewModel.sensitive != viewModel.editPostState.post!!.sensitive || mediaAttachmentsBeforeIds != mediaAttachmentsEditIds || viewModel.editPostState.post!!.place != viewModel.location) {
                            if (viewModel.editPostState.isLoading) {
                                Button(
                                    onClick = { }, modifier = Modifier.width(120.dp)
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(20.dp),
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                }
                            } else {
                                Button(
                                    onClick = { showSaveAlert = true },
                                    modifier = Modifier.width(120.dp)
                                ) {
                                    Text(text = stringResource(Res.string.save))
                                }
                            }
                        } else {
                            Button(
                                onClick = { }, enabled = false, modifier = Modifier.width(120.dp)
                            ) {
                                Text(text = stringResource(Res.string.save))
                            }
                        }
                    }
                })
        }) { paddingValues ->
        Box(Modifier.padding(paddingValues)) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(12.dp)
                    .verticalScroll(state = rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                viewModel.mediaAttachmentsEdit.forEachIndexed { index, mediaAttachment ->
                    Row(
                        Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(contentAlignment = Alignment.Center) {

                            if (mediaAttachment.type == "image") {
                                AsyncImage(
                                    model = mediaAttachment.url,
                                    contentDescription = null,
                                    modifier = Modifier.width(100.dp)
                                )
                            } else {
                                val model = ImageRequest.Builder(context).data(mediaAttachment.url)
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
                            }
                        }

                        Spacer(Modifier.width(10.dp))

                        val mediaDescriptionItem =
                            viewModel.mediaDescriptionItems.find { mediaDescriptionItem -> mediaDescriptionItem.imageId == mediaAttachment.id }
                                ?: EditPostViewModel.MediaDescriptionItem(
                                    mediaAttachment.id, "", false
                                )
                        val indexOfDescriptionItem =
                            viewModel.mediaDescriptionItems.indexOf(mediaDescriptionItem)
                        TextField(
                            value = mediaDescriptionItem.description,
                            onValueChange = {
                                val oldMediaAttachment =
                                    viewModel.editPostState.post!!.mediaAttachments.find { it.id == mediaDescriptionItem.imageId }
                                val changed = it != (oldMediaAttachment!!.description ?: "")
                                viewModel.mediaDescriptionItems[indexOfDescriptionItem] =
                                    viewModel.mediaDescriptionItems[indexOfDescriptionItem].copy(
                                        description = it, changed = changed
                                    )
                            },
                            modifier = Modifier.weight(1f),
                            singleLine = false,
                            placeholder = { Text(stringResource(Res.string.content_warning_or_spoiler_text)) },
                            shape = RoundedCornerShape(16.dp),
                            colors = TextFieldDefaults.colors(
                                unfocusedIndicatorColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                            ),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Default),
                            keyboardActions = KeyboardActions(onDone = {
                                keyboardController?.hide()
                                focusManager.clearFocus()
                            })
                        )

                        if (viewModel.mediaAttachmentsEdit.size > 1) {
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
                            IconButton(onClick = {
                                viewModel.deleteMediaDialog = mediaAttachment.id
                            }) {
                                Icon(
                                    imageVector = Icons.Outlined.Delete,
                                    contentDescription = "delete Image",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
                if (!viewModel.editPostState.isLoading && viewModel.editPostState.post != null) {
                    TextFieldMentionsComposable(submit = {},
                        text = viewModel.caption,
                        changeText = { text -> viewModel.caption = text },
                        labelStringId = Res.string.caption,
                        modifier = Modifier.fillMaxWidth(),
                        imeAction = ImeAction.Default,
                        suggestionsBoxColor = MaterialTheme.colorScheme.surfaceContainer,
                        submitButton = null
                    )

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = stringResource(Res.string.sensitive_nsfw_media))
                        Switch(checked = viewModel.sensitive,
                            onCheckedChange = { viewModel.sensitive = it })
                    }
                    if (viewModel.sensitive) {
                        TextField(
                            value = viewModel.sensitiveText,
                            singleLine = false,
                            onValueChange = { viewModel.sensitiveText = it },
                            placeholder = { Text(stringResource(Res.string.content_warning_or_spoiler_text)) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = TextFieldDefaults.colors(
                                unfocusedIndicatorColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                            ),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Default),
                            keyboardActions = KeyboardActions(onDone = {
                                keyboardController?.hide()
                                focusManager.clearFocus()
                            })
                        )
                    }
                    TextFieldLocationsComposable(submit = {}, submitPlace = {viewModel._setLocation(it)},
                        initialValue = viewModel.editPostState.post!!.place,
                        labelStringId = Res.string.location,
                        modifier = Modifier.fillMaxWidth(),
                        imeAction = ImeAction.Default,
                        suggestionsBoxColor = MaterialTheme.colorScheme.surfaceContainer,
                        submitButton = null
                    )
                }

                LoadingComposable(isLoading = viewModel.editPostState.isLoading)
                ErrorComposable(message = viewModel.editPostState.error)
                Spacer(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.ime))
            }

            if (showSaveAlert) {
                AlertDialog(title = {
                    Text(text = stringResource(Res.string.sure_update_post))
                }, onDismissRequest = {
                    showSaveAlert = false
                }, dismissButton = {
                    TextButton(onClick = {
                        showSaveAlert = false
                    }) {
                        Text(stringResource(Res.string.cancel))
                    }
                }, confirmButton = {
                    TextButton(onClick = {
                        showSaveAlert = false
                        viewModel.updatePost(postId, navController)
                    }) {
                        Text(stringResource(Res.string.save))
                    }
                })
            }
        }
    }

    if (viewModel.deleteMediaDialog != null) {
        AlertDialog(icon = {
            Icon(imageVector = Icons.Outlined.Delete, contentDescription = null)
        }, title = {
            Text(text = "Remove Media")
        }, text = {
            Text(text = "Are you sure you want to delete this media")
        }, onDismissRequest = {
            viewModel.deleteMediaDialog = null
        }, confirmButton = {
            TextButton(onClick = {
                viewModel.deleteMedia(viewModel.deleteMediaDialog!!)
            }) {
                Text(stringResource(Res.string.delete))
            }
        }, dismissButton = {
            TextButton(onClick = {
                viewModel.deleteMediaDialog = null
            }) {
                Text(stringResource(Res.string.cancel))
            }
        })
    }
}