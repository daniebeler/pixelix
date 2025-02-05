package com.daniebeler.pfpixelix.ui.composables.settings.icon_selection

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.daniebeler.pfpixelix.di.injectViewModel
import androidx.navigation.NavController
import org.jetbrains.compose.resources.vectorResource
import pixelix.app.generated.resources.Res
import pixelix.app.generated.resources.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IconSelectionComposable(
    navController: NavController,
    viewModel: IconSelectionViewModel = injectViewModel(key = "iconselectionvm") { iconSelectionViewModel }
) {

    val context = LocalContext.current

    val newIconName = remember { mutableStateOf("") }

    val lazyGridState = rememberLazyGridState()

    LaunchedEffect(Unit) {
        viewModel.fillList(context)
    }

    Scaffold(contentWindowInsets = WindowInsets.systemBars.only(WindowInsetsSides.Top), topBar = {
        CenterAlignedTopAppBar(title = {
            Text("Icon Selection", fontWeight = FontWeight.Bold)
        }, navigationIcon = {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(
                    imageVector = vectorResource(Res.drawable.chevron_back_outline), contentDescription = ""
                )
            }
        })

    }) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            LazyVerticalGrid(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp),
                state = lazyGridState,
                columns = GridCells.Fixed(3)
            ) {
                item (span = { GridItemSpan(3) }) {
                    Column {
                        Row {
                            Text(text = stringResource(Res.string.two_icons_info))
                        }

                        HorizontalDivider(Modifier.padding(vertical = 12.dp))
                    }

                }

                items(viewModel.icons) {
                    if (it.enabled) {
                        Box(
                            modifier = Modifier
                                .border(
                                    BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                                    shape = CircleShape
                                )
                                .padding(6.dp)
                        ) {
                            Image(
                                it.icon,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(1f)
                            )
                        }
                    } else {
                        Box(
                            modifier = Modifier.padding(6.dp)
                        ) {
                            Image(it.icon,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(1f)
                                    .clickable {
                                        newIconName.value = it.name
                                    })
                        }
                    }

                }
            }


        }

        if (newIconName.value.isNotBlank()) {
            AlertDialog(title = {
                Text(text = stringResource(Res.string.change_app_icon))
            }, text = {
                Text(text = stringResource(Res.string.change_app_icon_dialog_content))
            }, onDismissRequest = {
                newIconName.value = ""
            }, confirmButton = {
                TextButton(onClick = {
                    viewModel.changeIcon(context = context, newIconName.value)
                    newIconName.value = ""
                }) {
                    Text(stringResource(Res.string.change))
                }
            }, dismissButton = {
                TextButton(onClick = {
                    newIconName.value = ""
                }) {
                    Text(stringResource(Res.string.cancel))
                }
            })
        }

    }
}