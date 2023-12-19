package com.daniebeler.pixels.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.daniebeler.pixels.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeComposable(viewModel: MainViewModel, navController: NavController) {

    val items = viewModel.homeTimeline
    //viewModel.getHomeTimeline()
    
    val validatedToken = viewModel.verified


    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold (
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                scrollBehavior = scrollBehavior,
                title = {
                    Text("Home")
                }
            )

        }
    ) {paddingValues ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(32.dp),
            modifier = Modifier.padding(paddingValues)
        ) {
            item {
                Button(onClick = {
                    viewModel.checkToken()
                }) {
                    Text(text = "check token")
                }
            }

            item {
                Button(onClick = {
                    viewModel.getHomeTimeline()
                }) {
                    Text(text = "load home feed")
                }
            }

            if (validatedToken != null) {
                item {
                    Text(text = validatedToken.username)
                }
            }
            items(items) { item ->
                PostComposable(post = item, navController)
            }
        }
    }
}