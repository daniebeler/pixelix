package com.daniebeler.pixelix.ui.composables.settings.about_instance

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.daniebeler.pixelix.R
import com.daniebeler.pixelix.ui.composables.ErrorComposable
import com.daniebeler.pixelix.ui.composables.LoadingComposable
import com.daniebeler.pixelix.utils.Navigate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutInstanceComposable(
    navController: NavController,
    viewModel: AboutInstanceViewModel = hiltViewModel()
) {

    val lazyListState = rememberLazyListState()

    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(0, 0, 0, 0),
                title = {
                    viewModel.instanceState.instance?.domain?.let { Text(text = it) }
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
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues), state = lazyListState
        ) {

            if (viewModel.instanceState.isLoading) {
                item {
                    LoadingComposable(isLoading = true)
                }
            } else {
                item {
                    AsyncImage(
                        model = viewModel.instanceState.instance?.thumbnailUrl,
                        contentDescription = null,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                    Text(
                        text = viewModel.instanceState.instance?.description
                            ?: "",
                        Modifier.padding(12.dp, 0.dp)
                    )
                    Spacer(modifier = Modifier.height(18.dp))

                    Text(
                        text = stringResource(R.string.stats),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier
                            .padding(12.dp, 0.dp)
                    )

                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = viewModel.instanceState.instance?.stats?.userCount.toString(),
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                            Text(text = stringResource(R.string.users), fontSize = 12.sp)
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = viewModel.instanceState.instance?.stats?.statusCount.toString(),
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                            Text(text = stringResource(id = R.string.posts), fontSize = 12.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Text(
                        text = stringResource(R.string.admin),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier
                            .padding(12.dp, 0.dp)
                    )

                    if (viewModel.instanceState.instance != null) {
                        val account = viewModel.instanceState.instance!!.admin
                        Row(
                            modifier = Modifier
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                                .fillMaxWidth()
                                .clickable {
                                    Navigate().navigate(
                                        "profile_screen/" + account.id,
                                        navController
                                    )
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                model = account.avatar, contentDescription = "",
                                modifier = Modifier
                                    .height(46.dp)
                                    .width(46.dp)
                                    .clip(CircleShape)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(text = account.displayname)
                                Text(text = "@${account.username}")
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Text(
                        text = stringResource(R.string.privacy_policy),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier
                            .padding(12.dp, 0.dp)
                    )

                    Text(
                        text = "https://" + viewModel.instanceState.instance?.domain + "/site/privacy",
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .padding(12.dp, 0.dp)
                            .clickable {
                                if (viewModel.instanceState.instance != null) {
                                    Navigate().openUrlInApp(
                                        context = context,
                                        url = "https://" + viewModel.instanceState.instance!!.domain + "/site/privacy"
                                    )
                                }
                            })


                    Spacer(modifier = Modifier.height(18.dp))


                    Text(
                        text = stringResource(R.string.terms_of_use),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier
                            .padding(12.dp, 0.dp)
                    )

                    Text(
                        text = "https://" + viewModel.instanceState.instance?.domain + "/site/terms",
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .padding(12.dp, 0.dp)
                            .clickable {
                                if (viewModel.instanceState.instance != null) {
                                    Navigate().openUrlInApp(
                                        context = context,
                                        url = "https://" + viewModel.instanceState.instance!!.domain + "/site/terms"
                                    )
                                }
                            })


                    Spacer(modifier = Modifier.height(18.dp))

                    Text(
                        text = stringResource(R.string.rules),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier
                            .padding(12.dp, 0.dp)
                    )
                }

                items(viewModel.instanceState.instance?.rules ?: emptyList()) {
                    Row(modifier = Modifier.padding(vertical = 12.dp, horizontal = 12.dp)) {
                        Text(
                            text = it.id,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(18.dp))
                        Text(text = it.text)
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(18.dp))

                    Text(
                        text = stringResource(R.string.instance_version),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier
                            .padding(12.dp, 0.dp)
                    )

                    Text(
                        text = viewModel.instanceState.instance?.version ?: "",
                        modifier = Modifier
                            .padding(12.dp, 0.dp)
                    )
                }
            }

        }

        ErrorComposable(message = viewModel.instanceState.error)

    }
}