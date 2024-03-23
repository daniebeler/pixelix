package com.daniebeler.pfpixelix.ui.composables.edit_profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.daniebeler.pfpixelix.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileComposable(
    navController: NavController, viewModel: EditProfileViewModel = hiltViewModel()
) {

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(contentWindowInsets = WindowInsets(0.dp),
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(windowInsets = WindowInsets(0, 0, 0, 0),
                scrollBehavior = scrollBehavior,
                title = {
                    Text(text = stringResource(R.string.edit_profile))
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
                    if (viewModel.displayname == (viewModel.accountState.account?.displayname
                            ?: "")
                    ) {
                        Button(onClick = {}, enabled = false) {
                            Text(text = stringResource(R.string.save))
                        }
                    } else {
                        Button(onClick = {

                        }) {
                            Text(text = stringResource(R.string.save))
                        }
                    }

                })
        }) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            Column(
                Modifier
                    .fillMaxSize()
                    .verticalScroll(state = rememberScrollState())
            ) {

                if (viewModel.accountState.account != null) {
                    OutlinedTextField(
                        value = viewModel.displayname,
                        onValueChange = { viewModel.displayname = it })
                }

            }
        }

    }
}