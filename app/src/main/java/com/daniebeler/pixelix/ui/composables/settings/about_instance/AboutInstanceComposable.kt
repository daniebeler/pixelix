package com.daniebeler.pixelix.ui.composables.settings.about_instance

import android.content.ClipData.Item
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.daniebeler.pixelix.ui.composables.ErrorComposable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutInstanceComposable(
    navController: NavController,
    viewModel: AboutInstanceViewModel = hiltViewModel()
) {

    val lazyListState = rememberLazyListState()

    Scaffold(
        topBar = {
            TopAppBar(
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
                    text = "Rules",
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
        }

        ErrorComposable(message = viewModel.instanceState.error)

    }
}