package com.daniebeler.pfpixelix.ui.composables.stories

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.QuestionMark
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.daniebeler.pfpixelix.R
import com.daniebeler.pfpixelix.domain.model.StoryUser

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreviewRow(viewModel: PreviewRowViewModel = hiltViewModel(key = "preview-row-vm")) {

    val context = LocalContext.current

    LazyRow(
        modifier = Modifier.padding(top = 12.dp), contentPadding = PaddingValues(horizontal = 12.dp)
    ) {
        if (viewModel.storiesCarousel.carousel != null) {
            item {
                MyStory(
                    account = viewModel.storiesCarousel.carousel!!.self.user,
                    haveStories = viewModel.storiesCarousel.carousel!!.self.nodes.isNotEmpty()
                ) {
                    viewModel.showMyStory = true
                }
            }

            items(viewModel.storiesCarousel.carousel!!.nodes) {
                Story(account = it.user) { viewModel.showStory = true }
            }
        }
    }

    if (viewModel.showStory) {
        Dialog(
            onDismissRequest = { viewModel.showStory = false }, properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = false,
                usePlatformDefaultWidth = false
            )
        ) {
            /* Your full screen content */

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Yellow)
            ) {
                AsyncImage(
                    model = viewModel.storiesCarousel.carousel!!.nodes[0].nodes[0].src,
                    contentDescription = "",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )

                Column {
                    Row(Modifier.padding(top = 12.dp)) {
                        for (i in 0 until 3) {

                            var progress by remember { mutableStateOf(0F) }
                            val progressAnimDuration = 10000
                            val progressAnimation by animateFloatAsState(
                                targetValue = progress,
                                animationSpec = tween(durationMillis = progressAnimDuration),
                            )
                            LinearProgressIndicator(
                                progress = { progressAnimation },
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 6.dp)
                            )

                            LaunchedEffect(LocalLifecycleOwner.current) {
                                progress = 1F
                            }
                        }
                    }

                    Row {
                        AsyncImage(
                            model = viewModel.storiesCarousel.carousel!!.nodes[0].user.avatar,
                            contentDescription = "",
                            modifier = Modifier
                                .height(24.dp)
                                .width(24.dp)
                                .clip(CircleShape)
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(text = viewModel.storiesCarousel.carousel!!.nodes[0].user.username)
                    }
                }


            }
        }
    }


    if (viewModel.showMyStory) {
        Dialog(
            onDismissRequest = { viewModel.showMyStory = false }, properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = false,
                usePlatformDefaultWidth = false
            )
        ) {
            /* Your full screen content */

            val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

            Scaffold(contentWindowInsets = WindowInsets(0.dp),
                modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                topBar = {
                    TopAppBar(windowInsets = WindowInsets(0, 0, 0, 0), title = {
                        Text(
                            "My Stories", fontWeight = FontWeight.Bold
                        )
                    }, actions = {

                        Button(onClick = { viewModel.openNewStoryPage(context = context) }) {
                            Text(text = "New story")
                        }

                        IconButton(onClick = { viewModel.showMyStory = false }) {
                            Icon(
                                imageVector = Icons.Outlined.Close, contentDescription = null
                            )
                        }
                    })

                }) { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {

                    Column {

                        Spacer(modifier = Modifier.height(32.dp))

                        Text(
                            text = "My Story",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Box(modifier = Modifier.padding(32.dp)) {
                            AsyncImage(
                                model = viewModel.storiesCarousel.carousel!!.self.nodes[0].src,
                                contentDescription = "",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(12.dp)),
                                contentScale = ContentScale.Fit,
                            )
                        }
                    }
                }

            }


        }
    }


}

@Composable
private fun Story(account: StoryUser, onClick: () -> Unit) {
    Column {
        AsyncImage(model = account.avatar,
            contentDescription = "",
            modifier = Modifier
                .size(70.dp)
                .clip(
                    CircleShape
                )
                .border( // Add a border to the image
                    width = 2.dp, // The width of the border is 2dp
                    brush = Brush.linearGradient( // The color of the border is a linear gradient
                        colors = listOf(Color.Yellow, Color.Red), // The colors of the gradient
                        start = Offset(0f, 0f), // The start point of the gradient
                        end = Offset(70f, 70f) // The end point of the gradient
                    ), shape = CircleShape // The shape of the border is a circle
                )
                .clickable {
                    onClick()
                })

        Spacer(modifier = Modifier.height(12.dp))
        Text(text = account.username, color = MaterialTheme.colorScheme.onSurface)
    }
}

@Composable
private fun MyStory(account: StoryUser, haveStories: Boolean, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        if (haveStories) {
            AsyncImage(model = account.avatar,
                contentDescription = "",
                modifier = Modifier
                    .size(70.dp)
                    .clip(
                        CircleShape
                    )
                    .border( // Add a border to the image
                        width = 2.dp, // The width of the border is 2dp
                        brush = Brush.linearGradient( // The color of the border is a linear gradient
                            colors = listOf(Color.Yellow, Color.Red), // The colors of the gradient
                            start = Offset(0f, 0f), // The start point of the gradient
                            end = Offset(70f, 70f) // The end point of the gradient
                        ), shape = CircleShape // The shape of the border is a circle
                    )
                    .clickable {
                        onClick()
                    })
        } else {
            AsyncImage(model = account.avatar,
                contentDescription = "",
                modifier = Modifier
                    .size(70.dp)
                    .clip(
                        CircleShape
                    )
                    .clickable {
                        onClick()
                    })
        }


        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = account.username,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.width(80.dp),
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis
        )
    }
}