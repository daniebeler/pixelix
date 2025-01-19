package com.daniebeler.pfpixelix.ui.composables.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.daniebeler.pfpixelix.R
import com.daniebeler.pfpixelix.ui.composables.InfiniteListHandler
import com.daniebeler.pfpixelix.utils.Navigate

@Composable
fun CollectionsComposable(
    collectionsState: CollectionsState,
    getMoreCollections: () -> Unit,
    navController: NavController,
    addNewButton: Boolean = false,
    instanceDomain: String,
    openUrl: (url: String) -> Unit
) {
    val lazyListState = rememberLazyListState()
    var showAddCollectionDialog = remember {
        mutableStateOf(false)
    }

    if (addNewButton || collectionsState.collections.isNotEmpty()) {
        Column {
            HorizontalDivider(Modifier.padding(12.dp))

            Text(
                text = stringResource(R.string.collections),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(start = 12.dp)
            )

            LazyRow(state = lazyListState) {
                items(collectionsState.collections) {
                    Column(
                        Modifier
                            .padding(12.dp)
                            .clickable {
                                Navigate.navigate("collection_screen/" + it.id, navController)
                            }, horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AsyncImage(
                            model = it.thumbnail,
                            contentDescription = "",
                            modifier = Modifier
                                .height(84.dp)
                                .width(84.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop,
                            alignment = Alignment.Center,
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = it.title)
                    }
                }
                if (collectionsState.isLoading) {
                    item {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .fillMaxHeight()
                                .height(96.dp)
                                .wrapContentSize(Alignment.Center)
                        )
                    }
                }
                if (addNewButton) {
                    item {
                        Column(
                            Modifier
                                .padding(12.dp)
                                .clickable {
                                    showAddCollectionDialog.value = true
                                }, horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .height(84.dp)
                                    .width(84.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.surfaceContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Add,
                                    contentDescription = "add collection",
                                    Modifier.size(32.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = stringResource(R.string.new_))
                        }
                    }
                }
            }
        }

        InfiniteListHandler(lazyListState) {
            getMoreCollections()
        }

        if (showAddCollectionDialog.value) {
            AlertDialog(title = {
                Text(text = stringResource(R.string.new_collection))
            }, text = {
                Column {
                    Text(text = stringResource(R.string.collection_create_not_supported_explanation))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "$instanceDomain/i/collections/create",
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable(onClick = { openUrl("https://$instanceDomain/i/collections/create") })
                    )
                }
            }, onDismissRequest = {
                showAddCollectionDialog.value = false
            }, confirmButton = {
                TextButton(onClick = {
                    showAddCollectionDialog.value = false
                }) {
                    Text("Ok")
                }
            })
        }
    }



}