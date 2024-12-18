package com.daniebeler.pfpixelix.ui.composables.edit_profile

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.daniebeler.pfpixelix.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileComposable(
    navController: NavController,
    viewModel: EditProfileViewModel = hiltViewModel(key = "edit-profile-viewmodel-key")
) {

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val context = LocalContext.current

    val imageCropLauncher = rememberLauncherForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            // use the cropped image
            if (result.uriContent != null) {
                viewModel.avatarUri = result.uriContent!!
                viewModel.avatarChanged = true
            }
        } else {
            // an error occurred cropping
            println(result.error)
        }
    }


    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                val cropOptions = CropImageContractOptions(
                    uri, CropImageOptions(
                        fixAspectRatio = true,
                        aspectRatioX = 1,
                        aspectRatioY = 1,
                        cropShape = CropImageView.CropShape.OVAL
                    )
                )
                imageCropLauncher.launch(cropOptions)
            }
        })

    Scaffold(contentWindowInsets = WindowInsets(0.dp),
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(windowInsets = WindowInsets(0, 0, 0, 0),
                scrollBehavior = scrollBehavior,
                title = {
                    Text(text = stringResource(R.string.edit_profile), fontWeight = FontWeight.Bold)
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
                    if (viewModel.firstLoaded) {
                        if (viewModel.displayname == (viewModel.accountState.account?.displayname
                                ?: "") && viewModel.note == (viewModel.accountState.account?.note
                                ?: "") && "https://" + viewModel.website == (viewModel.accountState.account?.website
                                ?: "") && !viewModel.avatarChanged && viewModel.privateProfile == viewModel.accountState.account?.locked
                        ) {
                            if (!viewModel.accountState.isLoading) {
                                Button(
                                    onClick = {}, modifier = Modifier.width(120.dp), enabled = false
                                ) {
                                    Text(text = stringResource(R.string.save))
                                }
                            }
                        } else {
                            if (viewModel.accountState.isLoading) {
                                Button(onClick = {}, modifier = Modifier.width(120.dp)) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(20.dp),
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                }
                            } else {
                                Button(onClick = {
                                    viewModel.save(context)
                                }, modifier = Modifier.width(120.dp)) {
                                    Text(text = stringResource(R.string.save))
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
                        AsyncImage(model = viewModel.avatarUri,
                            contentDescription = "",
                            modifier = Modifier
                                .height(112.dp)
                                .width(112.dp)
                                .clip(CircleShape)
                                .clickable {
                                    singlePhotoPickerLauncher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                })
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(value = viewModel.displayname,
                        singleLine = true,
                        onValueChange = { viewModel.displayname = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text(text = "Displayname")
                        })

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(value = viewModel.note,
                        onValueChange = { viewModel.note = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text(text = "Note")
                        })

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(value = viewModel.website,
                        singleLine = true,
                        onValueChange = { viewModel.website = it },
                        modifier = Modifier.fillMaxWidth(),
                        prefix = {
                            Text(text = "https://")
                        },
                        label = {
                            Text(text = "Website")
                        })

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = stringResource(R.string.private_profile))
                        Spacer(modifier = Modifier.weight(1f))
                        Switch(checked = viewModel.privateProfile,
                            onCheckedChange = { viewModel.privateProfile = it })
                    }

                }

            }
        }

    }
}