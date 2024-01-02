package com.daniebeler.pixels.ui.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.daniebeler.pixels.MainViewModel
import com.daniebeler.pixels.R
import com.daniebeler.pixels.ui.composables.timelines.global_timeline.GlobalTimelineComposable
import com.daniebeler.pixels.ui.composables.timelines.home_timeline.HomeTimelineComposable
import com.daniebeler.pixels.ui.composables.timelines.local_timeline.LocalTimelineComposable
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeComposable(viewModel: MainViewModel, navController: NavController) {

    val pagerState = rememberPagerState { 3 }

    val scope = rememberCoroutineScope()


    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(id = R.string.app_name))
                }
            )

        }
    ) { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            PrimaryTabRow(selectedTabIndex = pagerState.currentPage) {
                Tab(
                    text = { Text("Home") },
                    selected = pagerState.currentPage == 0,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(0)
                        }

                    })

                Tab(
                    text = { Text("Local") },
                    selected = pagerState.currentPage == 0,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(1)
                        }
                    })

                Tab(
                    text = { Text("Global") },
                    selected = pagerState.currentPage == 0,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(2)
                        }
                    })
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .weight(1f)
                    .background(MaterialTheme.colorScheme.background)
            ) { tabIndex ->
                when (tabIndex) {
                    0 ->
                        Box(modifier = Modifier.fillMaxSize()) {
                            HomeTimelineComposable(navController)
                        }

                    1 ->
                        Box(modifier = Modifier.fillMaxSize()) {
                            LocalTimelineComposable(navController)
                        }

                    2 ->
                        Box(modifier = Modifier.fillMaxSize()) {
                            GlobalTimelineComposable(navController)
                        }
                }
            }
        }
    }
}