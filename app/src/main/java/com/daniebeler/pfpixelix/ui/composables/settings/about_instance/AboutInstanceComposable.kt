package com.daniebeler.pfpixelix.ui.composables.settings.about_instance

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.daniebeler.pfpixelix.R
import com.daniebeler.pfpixelix.ui.composables.states.FullscreenErrorComposable
import com.daniebeler.pfpixelix.ui.composables.states.FullscreenLoadingComposable
import com.daniebeler.pfpixelix.utils.Navigate
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutInstanceComposable(
    navController: NavController,
    viewModel: AboutInstanceViewModel = hiltViewModel(key = "about-instance-key")
) {

    val lazyListState = rememberLazyListState()

    val context = LocalContext.current

    Scaffold(contentWindowInsets = WindowInsets.systemBars.only(WindowInsetsSides.Top), topBar = {
        CenterAlignedTopAppBar(title = {
            Text(text = viewModel.ownInstanceDomain, fontWeight = FontWeight.Bold)
        }, navigationIcon = {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.chevron_back_outline), contentDescription = ""
                )
            }
        })
    }) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues), state = lazyListState
        ) {
            if (!viewModel.instanceState.isLoading && viewModel.instanceState.error.isEmpty()) {
                item {
                    AsyncImage(
                        model = viewModel.instanceState.instance?.thumbnailUrl,
                        contentDescription = null,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                    Text(
                        text = viewModel.instanceState.instance?.description ?: "",
                        Modifier.padding(12.dp, 0.dp)
                    )
                    Spacer(modifier = Modifier.height(18.dp))

                    Text(
                        text = stringResource(R.string.stats),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(12.dp, 0.dp)
                    )

                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = String.format(
                                    Locale.GERMANY,
                                    "%,d",
                                    viewModel.instanceState.instance?.stats?.userCount
                                ), fontWeight = FontWeight.Bold, fontSize = 18.sp
                            )
                            Text(text = stringResource(R.string.users), fontSize = 12.sp)
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = String.format(
                                    Locale.GERMANY,
                                    "%,d",
                                    viewModel.instanceState.instance?.stats?.statusCount
                                ), fontWeight = FontWeight.Bold, fontSize = 18.sp
                            )
                            Text(text = stringResource(id = R.string.posts), fontSize = 12.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    viewModel.instanceState.instance?.admin?.let { account ->
                        Text(
                            text = stringResource(R.string.admin),
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(12.dp, 0.dp)
                        )

                        Row(modifier = Modifier
                            .clickable {
                                Navigate.navigate(
                                    "profile_screen/" + account.id, navController
                                )
                            }
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                            .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically) {
                            AsyncImage(
                                model = account.avatar,
                                error = painterResource(id = R.drawable.default_avatar),
                                contentDescription = "",
                                modifier = Modifier
                                    .height(46.dp)
                                    .width(46.dp)
                                    .clip(CircleShape)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                if (account.displayname != null) {
                                    Text(text = account.displayname)
                                }
                                Text(text = "@${account.username}")
                            }
                        }
                    }

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = stringResource(R.string.privacy_policy),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(12.dp, 0.dp)
                )

                Text(text = "https://" + viewModel.instanceState.instance?.domain + "/site/privacy",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(12.dp, 0.dp)
                        .clickable {
                            if (viewModel.instanceState.instance != null) {
                                viewModel.openUrl(
                                    url = "https://" + viewModel.instanceState.instance!!.domain + "/site/privacy"
                                )
                            }
                        })


                Spacer(modifier = Modifier.height(18.dp))


                Text(
                    text = stringResource(R.string.terms_of_use),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(12.dp, 0.dp)
                )

                Text(text = "https://" + viewModel.instanceState.instance?.domain + "/site/terms",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(12.dp, 0.dp)
                        .clickable {
                            if (viewModel.instanceState.instance != null) {
                                viewModel.openUrl(
                                    url = "https://" + viewModel.instanceState.instance!!.domain + "/site/terms"
                                )
                            }
                        })


                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = stringResource(R.string.rules),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(12.dp, 0.dp)
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
                    modifier = Modifier.padding(12.dp, 0.dp)
                )

                Text(
                    text = viewModel.instanceState.instance?.version ?: "",
                    modifier = Modifier.padding(12.dp, 0.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))
            }
        }

    }

    if (viewModel.instanceState.isLoading) {
        FullscreenLoadingComposable()
    }

    if (viewModel.instanceState.error.isNotBlank()) {
        FullscreenErrorComposable(message = viewModel.instanceState.error)
    }
}
}