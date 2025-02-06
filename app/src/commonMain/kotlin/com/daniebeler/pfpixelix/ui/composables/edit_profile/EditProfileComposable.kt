package com.daniebeler.pfpixelix.ui.composables.edit_profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.daniebeler.pfpixelix.di.injectViewModel
import com.daniebeler.pfpixelix.utils.KmpUri
import com.daniebeler.pfpixelix.utils.LocalKmpContext
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import pixelix.app.generated.resources.Res
import pixelix.app.generated.resources.bio
import pixelix.app.generated.resources.chevron_back_outline
import pixelix.app.generated.resources.displayname
import pixelix.app.generated.resources.edit_profile
import pixelix.app.generated.resources.private_profile
import pixelix.app.generated.resources.save
import pixelix.app.generated.resources.website

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileComposable(
    navController: NavController,
    viewModel: EditProfileViewModel = injectViewModel(key = "edit-profile-viewmodel-key") { editProfileViewModel }
) {

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val context = LocalKmpContext.current

    Scaffold(contentWindowInsets = WindowInsets.systemBars.only(WindowInsetsSides.Top),
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(scrollBehavior = scrollBehavior,
                title = {
                    Text(text = stringResource(Res.string.edit_profile), fontWeight = FontWeight.Bold)
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = vectorResource(Res.drawable.chevron_back_outline),
                            contentDescription = ""
                        )
                    }
                },
                actions = {
                    if (viewModel.firstLoaded) {
                        if (viewModel.displayname == (viewModel.accountState.account?.displayname
                                ?: "") && viewModel.note == (viewModel.accountState.account?.note
                                ?: "") && "https://" + viewModel.website == (viewModel.accountState.account?.website
                                ?: "") && !viewModel.avatarChanged && viewModel.privateProfile == viewModel.accountState.account?.locked
                        ) {
                            if (!viewModel.accountState.isLoading) {
                                Button(
                                    onClick = {},
                                    modifier = Modifier.width(120.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    enabled = false,
                                    colors = ButtonDefaults.buttonColors(
                                        disabledContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                                        disabledContentColor = MaterialTheme.colorScheme.onSurface
                                    )
                                ) {
                                    Text(text = stringResource(Res.string.save))
                                }
                            }
                        } else {
                            if (viewModel.accountState.isLoading) {
                                Button(
                                    onClick = {},
                                    modifier = Modifier.width(120.dp),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(20.dp),
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                }
                            } else {
                                Button(
                                    onClick = {
                                        viewModel.save(context)
                                    },
                                    modifier = Modifier.width(120.dp),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(text = stringResource(Res.string.save))
                                }
                            }
                        }
                    }
                })
        }) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(12.dp)
                    .verticalScroll(state = rememberScrollState())
            ) {

                if (viewModel.accountState.account != null) {

                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        val avatarPicker = rememberAvatarPicker { uri ->
                            viewModel.avatarUri = uri
                            viewModel.avatarChanged = true
                        }
                        AsyncImage(model = viewModel.avatarUri,
                            contentDescription = "",
                            modifier = Modifier
                                .height(112.dp)
                                .width(112.dp)
                                .clip(CircleShape)
                                .clickable {
                                    avatarPicker()
                                })
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Row {
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = stringResource(Res.string.displayname),
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(Modifier.height(6.dp))

                    TextField(
                        value = viewModel.displayname,
                        singleLine = true,
                        onValueChange = { viewModel.displayname = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = TextFieldDefaults.colors(
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer
                        )
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Row {
                        Spacer(Modifier.width(6.dp))
                        Text(text = stringResource(Res.string.bio), fontWeight = FontWeight.Bold)
                    }

                    Spacer(Modifier.height(6.dp))


                    TextField(
                        value = viewModel.note,
                        onValueChange = { viewModel.note = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = TextFieldDefaults.colors(
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer
                        )
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Row {
                        Spacer(Modifier.width(6.dp))
                        Text(text = stringResource(Res.string.website), fontWeight = FontWeight.Bold)
                    }

                    Spacer(Modifier.height(6.dp))

                    TextField(
                        value = viewModel.website,
                        singleLine = true,
                        prefix = {
                            Text(text = "https://")
                        },
                        onValueChange = { viewModel.website = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = TextFieldDefaults.colors(
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer
                        )
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = stringResource(Res.string.private_profile),
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Switch(checked = viewModel.privateProfile,
                            onCheckedChange = { viewModel.privateProfile = it })
                    }

                }

            }
        }

    }
}

@Composable
expect fun rememberAvatarPicker(callback: (KmpUri) -> Unit): () -> Unit