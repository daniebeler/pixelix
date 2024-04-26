package com.daniebeler.pfpixelix.ui.composables.edit_post

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
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
import com.daniebeler.pfpixelix.ui.composables.states.ErrorComposable
import com.daniebeler.pfpixelix.ui.composables.states.LoadingComposable
import com.daniebeler.pfpixelix.ui.composables.textfield_mentions.TextFieldMentionsComposable

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun EditPostComposable(
    postId: String, navController: NavController, viewModel: EditPostViewModel = hiltViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    var showSaveAlert by remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.loadData(postId)
    }

    Scaffold(contentWindowInsets = WindowInsets(0.dp),
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(windowInsets = WindowInsets(0, 0, 0, 0),
                scrollBehavior = scrollBehavior,
                title = {
                    Text(
                        text = stringResource(id = R.string.edit_post), fontWeight = FontWeight.Bold
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
                        if (viewModel.mediaDescriptionItems.any { it.changed } || viewModel.caption.text != viewModel.editPostState.post!!.content || viewModel.sensitive != viewModel.editPostState.post!!.sensitive || (viewModel.sensitive == viewModel.editPostState.post!!.sensitive && viewModel.sensitiveText != viewModel.editPostState.post!!.spoilerText)) {
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
                                    Text(text = stringResource(R.string.save))
                                }
                            }
                        } else {
                            Button(
                                onClick = { }, enabled = false, modifier = Modifier.width(120.dp)
                            ) {
                                Text(text = stringResource(R.string.save))
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
                if (!viewModel.editPostState.isLoading && viewModel.editPostState.post != null) {
                    TextFieldMentionsComposable(submit = {},
                        text = viewModel.caption,
                        changeText = { text -> viewModel.caption = text },
                        labelStringId = R.string.caption,
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

                    viewModel.mediaAttachments.forEachIndexed { index, mediaAttachment ->
                        Row(
                            Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(contentAlignment = Alignment.Center) {

                                if (mediaAttachment.type == "image" && mediaAttachment.url?.takeLast(
                                        4
                                    ) != ".gif"
                                ) {
                                    AsyncImage(
                                        model = mediaAttachment.url,
                                        contentDescription = null,
                                        modifier = Modifier.width(100.dp)
                                    )
                                } else if (mediaAttachment.url?.takeLast(4) == ".gif") {
                                    GlideImage(
                                        model = mediaAttachment.url,
                                        contentDescription = null,
                                        modifier = Modifier.width(100.dp)
                                    )
                                } else {
                                    val model =
                                        ImageRequest.Builder(context).data(mediaAttachment.url)
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
                            OutlinedTextField(
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
                                label = { Text(stringResource(R.string.alt_text)) },
                            )

                            if (viewModel.mediaAttachments.size > 1) {
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

                LoadingComposable(isLoading = viewModel.editPostState.isLoading)
                ErrorComposable(message = viewModel.editPostState.error)
                Spacer(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.ime))
            }

            if (showSaveAlert) {
                AlertDialog(title = {
                    Text(text = stringResource(id = R.string.are_you_sure_you_want_to_log_out))
                }, onDismissRequest = {
                    showSaveAlert = false
                }, dismissButton = {
                    TextButton(onClick = {
                        showSaveAlert = false
                    }) {
                        Text(stringResource(id = R.string.cancel))
                    }
                }, confirmButton = {
                    TextButton(onClick = {
                        showSaveAlert = false
                        viewModel.updatePost(postId)
                    }) {
                        Text(stringResource(id = R.string.save))
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
                Text(stringResource(R.string.delete))
            }
        }, dismissButton = {
            TextButton(onClick = {
                viewModel.deleteMediaDialog = null
            }) {
                Text(stringResource(id = R.string.cancel))
            }
        })
    }
}