package com.daniebeler.pfpixelix.ui.composables.newpost

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowLeft
import androidx.compose.material.icons.automirrored.outlined.ArrowRight
import androidx.compose.material.icons.outlined.ArrowDownward
import androidx.compose.material.icons.outlined.ArrowRight
import androidx.compose.material.icons.outlined.ArrowUpward
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import co.touchlab.kermit.Logger
import coil3.compose.AsyncImage
import com.daniebeler.pfpixelix.common.Constants.AUDIENCE_FOLLOWERS_ONLY
import com.daniebeler.pfpixelix.common.Constants.AUDIENCE_PUBLIC
import com.daniebeler.pfpixelix.common.Constants.AUDIENCE_UNLISTED
import com.daniebeler.pfpixelix.di.injectViewModel
import com.daniebeler.pfpixelix.ui.composables.states.ErrorComposable
import com.daniebeler.pfpixelix.ui.composables.states.LoadingComposable
import com.daniebeler.pfpixelix.ui.composables.textfield_location.TextFieldLocationsComposable
import com.daniebeler.pfpixelix.ui.composables.textfield_mentions.TextFieldMentionsComposable
import com.daniebeler.pfpixelix.utils.KmpUri
import com.daniebeler.pfpixelix.utils.LocalKmpContext
import com.daniebeler.pfpixelix.utils.MimeType
import com.daniebeler.pfpixelix.utils.getPlatformUriObject
import com.daniebeler.pfpixelix.utils.imeAwareInsets
import com.daniebeler.pfpixelix.utils.toKmpUri
import io.github.vinceglb.filekit.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.core.PickerMode
import io.github.vinceglb.filekit.core.PickerType
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import pixelix.app.generated.resources.Res
import pixelix.app.generated.resources.add_outline
import pixelix.app.generated.resources.alt_text
import pixelix.app.generated.resources.audience
import pixelix.app.generated.resources.audience_public
import pixelix.app.generated.resources.cancel
import pixelix.app.generated.resources.caption
import pixelix.app.generated.resources.content_warning_or_spoiler_text
import pixelix.app.generated.resources.followers_only
import pixelix.app.generated.resources.location
import pixelix.app.generated.resources.new_post
import pixelix.app.generated.resources.release
import pixelix.app.generated.resources.sensitive_nsfw_media
import pixelix.app.generated.resources.trash_outline
import pixelix.app.generated.resources.unlisted

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewPostComposable(
    navController: NavController,
    uris: List<KmpUri>? = null,
    viewModel: NewPostViewModel = injectViewModel(key = "new-post-viewmodel-key") { newPostViewModel }
) {
    val context = LocalKmpContext.current

    var expanded by remember { mutableStateOf(false) }
    var showReleaseAlert by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(uris) {
        uris?.let {
            uris.forEach {
                viewModel.addImage(uri = it, context = context)
            }
        }
    }

    LaunchedEffect(viewModel.images) {
        Logger.d("images") {viewModel.images.none { it.isLoading }.toString()}
    }

    Scaffold(contentWindowInsets = WindowInsets.systemBars.only(WindowInsetsSides.Top), topBar = {
        CenterAlignedTopAppBar(title = {
            Text(text = stringResource(Res.string.new_post), fontWeight = FontWeight.Bold)
        }, actions = {
            Button(
                onClick = { showReleaseAlert = true },
                enabled = (viewModel.images.isNotEmpty() && viewModel.images.none { it.isLoading })
            ) {
                Text(text = stringResource(Res.string.release))
            }
        })
    }) { paddingValues ->
        Box {
            Column(
                Modifier.padding(paddingValues).imeAwareInsets(90.dp).fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ImagesPager(viewModel.images,
                    { index, altText ->
                        viewModel.updateAltTextVariable(
                            index, altText
                        )
                    },
                    { index -> viewModel.moveMediaAttachmentUp(index) },
                    { index -> viewModel.moveMediaAttachmentDown(index) },
                    { index -> viewModel.deleteMedia(index) })/*viewModel.images.forEachIndexed { index, image ->
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Box(contentAlignment = Alignment.Center) {

                            val type = MimeType.getMimeType(image.imageUri, context)
                            if (type != null && type.take(5) == "video") {
                                //todo KMP video
                                AsyncImage(
                                    model = image.imageUri.getPlatformUriObject(),
                                    contentDescription = "video thumbnail",
                                    modifier = Modifier.width(100.dp)
                                )
                            } else {
                                AsyncImage(
                                    model = image.imageUri.getPlatformUriObject(),
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

                        TextField(
                            value = image.text,
                            onValueChange = { viewModel.updateAltTextVariable(index, it) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(16.dp),
                            colors = TextFieldDefaults.colors(
                                unfocusedIndicatorColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer
                            ),
                            label = { Text(stringResource(Res.string.alt_text)) },
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
                                imageVector = vectorResource(Res.drawable.trash_outline),
                                contentDescription = "delete Image",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }*/
                Column(Modifier.padding(12.dp)) {

                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        val launcher = rememberFilePickerLauncher(
                            type = PickerType.ImageAndVideo, mode = PickerMode.Multiple()
                        ) { files ->
                            files?.forEach { file ->
                                viewModel.addImage(file.toKmpUri(), context)
                            }
                        }
                        Icon(
                            modifier = Modifier.clickable { launcher.launch() }.height(50.dp)
                                .width(50.dp),
                            imageVector = vectorResource(Res.drawable.add_outline),
                            contentDescription = null,
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
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
                            onValueChange = { viewModel.sensitiveText = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text(stringResource(Res.string.content_warning_or_spoiler_text)) },
                            shape = RoundedCornerShape(16.dp),
                            colors = TextFieldDefaults.colors(
                                unfocusedIndicatorColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer
                            )
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = stringResource(Res.string.audience))
                        Box {
                            OutlinedButton(onClick = { expanded = !expanded }) {
                                val buttonText: String = when (viewModel.audience) {
                                    AUDIENCE_PUBLIC -> {
                                        stringResource(Res.string.audience_public)
                                    }

                                    AUDIENCE_UNLISTED -> {
                                        stringResource(Res.string.unlisted)
                                    }

                                    AUDIENCE_FOLLOWERS_ONLY -> {
                                        stringResource(Res.string.followers_only)
                                    }

                                    else -> {
                                        ""
                                    }
                                }
                                Text(text = buttonText)
                            }
                            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                                DropdownMenuItem(text = { Text(stringResource(Res.string.audience_public)) },
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
                                DropdownMenuItem(text = { Text(stringResource(Res.string.unlisted)) },
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
                                DropdownMenuItem(text = { Text(stringResource(Res.string.followers_only)) },
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
                    TextFieldLocationsComposable(submit = { viewModel.setLocation(it) },
                        submitPlace = {},
                        initialValue = null,
                        labelStringId = Res.string.location,
                        modifier = Modifier.fillMaxWidth(),
                        imeAction = ImeAction.Default,
                        suggestionsBoxColor = MaterialTheme.colorScheme.surfaceContainer,
                        submitButton = null
                    )
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
                        Text(stringResource(Res.string.cancel))
                    }
                }, confirmButton = {
                    TextButton(onClick = {
                        showReleaseAlert = false
                        viewModel.post(navController)
                    }) {
                        Text(stringResource(Res.string.release))
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

@Composable
fun ImagesPager(
    images: List<NewPostViewModel.ImageItem>,
    updateAltText: (index: Int, altText: String) -> Unit,
    moveImageUp: (index: Int) -> Unit,
    moveImageDown: (index: Int) -> Unit,
    deleteMedia: (index: Int) -> Unit
) {
    val pagerState = rememberPagerState { images.size }
    val context = LocalKmpContext.current
    val scope = rememberCoroutineScope()

    HorizontalPager(
        state = pagerState, contentPadding = PaddingValues(horizontal = 32.dp), pageSpacing = 10.dp
    ) { page ->
        val image = images[page]
        Column(
            horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()
            ) {
                if (page == 0) {
                    Box(Modifier.width(48.dp)) {}
                } else {
                    IconButton(onClick = {
                        moveImageUp(page)
                        scope.launch {
                            pagerState.animateScrollToPage(page = page - 1)
                        }
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowLeft,
                            contentDescription = "move Image upwards"
                        )
                    }
                }
                    IconButton(onClick = {
                        deleteMedia(page)
                    }) {
                        Icon(
                            imageVector = vectorResource(Res.drawable.trash_outline),
                            contentDescription = "delete Image",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }


                if (page == images.size - 1) {
                    Box(Modifier.width(48.dp)) {}
                } else {
                    IconButton(onClick = {
                        moveImageDown(page)
                        scope.launch {
                            pagerState.animateScrollToPage(page = page + 1)
                        }
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowRight,
                            contentDescription = "move Image downwards"
                        )
                    }
                }
            }
            Card(Modifier.fillMaxWidth().aspectRatio(1f)) {

                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {

                    val type = MimeType.getMimeType(image.imageUri, context)
                    if (type != null && type.take(5) == "video") {
                        //todo KMP video
                        AsyncImage(
                            model = image.imageUri.getPlatformUriObject(),
                            contentDescription = "video thumbnail",
                            modifier = Modifier.width(100.dp)
                        )
                    } else {
                        AsyncImage(
                            model = image.imageUri.getPlatformUriObject(),
                            contentDescription = null,
                            modifier = Modifier.fillMaxWidth(),
                            contentScale = ContentScale.Inside
                        )
                    }
                    if (image.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.wrapContentSize(Alignment.Center)
                        )
                    }
                }
            }
            TextField(
                value = image.text,
                onValueChange = { updateAltText(page, it) },
                modifier = Modifier.fillMaxWidth().padding(top = 20.dp),
                shape = RoundedCornerShape(16.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer
                ),
                label = { Text(stringResource(Res.string.alt_text)) },
            )
        }
    }
}