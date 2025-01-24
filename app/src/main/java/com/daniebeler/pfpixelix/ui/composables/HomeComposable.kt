package com.daniebeler.pfpixelix.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material.icons.outlined.QuestionMark
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.daniebeler.pfpixelix.R
import com.daniebeler.pfpixelix.ui.composables.timelines.global_timeline.GlobalTimelineComposable
import com.daniebeler.pfpixelix.ui.composables.timelines.home_timeline.HomeTimelineComposable
import com.daniebeler.pfpixelix.ui.composables.timelines.local_timeline.LocalTimelineComposable
import com.daniebeler.pfpixelix.utils.Navigate
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeComposable(navController: NavController) {

    val pagerState = rememberPagerState { 3 }

    val scope = rememberCoroutineScope()

    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(contentWindowInsets = WindowInsets.systemBars.only(WindowInsetsSides.Top),topBar = {
        CenterAlignedTopAppBar(title = {
            Text(stringResource(id = R.string.app_name), fontWeight = FontWeight.Bold)
        },navigationIcon = {
            IconButton(onClick = { showBottomSheet = true }) {
                Icon(imageVector = Icons.Outlined.QuestionMark, contentDescription = "Help")
            }
        }, actions = {
            Row {

                IconButton(onClick = { Navigate.navigate("conversations", navController) }) {
                    Icon(imageVector = Icons.Outlined.Mail, contentDescription = "Conversations")
                }
                IconButton(onClick = { Navigate.navigate("preferences_screen", navController) }) {
                    Icon(imageVector = Icons.Outlined.Settings, contentDescription = "Settings")
                }
            }
        })

    }) { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            PrimaryTabRow(selectedTabIndex = pagerState.currentPage) {
                Tab(text = { Text(stringResource(R.string.home)) },
                    selected = pagerState.currentPage == 0,
                    selectedContentColor = MaterialTheme.colorScheme.primary,
                    unselectedContentColor = MaterialTheme.colorScheme.onBackground,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(0)
                        }
                    })

                Tab(text = { Text(stringResource(R.string.local)) },
                    selected = pagerState.currentPage == 1,
                    selectedContentColor = MaterialTheme.colorScheme.primary,
                    unselectedContentColor = MaterialTheme.colorScheme.onBackground,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(1)
                        }
                    })

                Tab(text = { Text(stringResource(R.string.global)) },
                    selected = pagerState.currentPage == 2,
                    selectedContentColor = MaterialTheme.colorScheme.primary,
                    unselectedContentColor = MaterialTheme.colorScheme.onBackground,
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
                    0 -> Box(modifier = Modifier.fillMaxSize()) {
                        HomeTimelineComposable(navController)
                    }

                    1 -> Box(modifier = Modifier.fillMaxSize()) {
                        LocalTimelineComposable(navController)
                    }

                    2 -> Box(modifier = Modifier.fillMaxSize()) {
                        GlobalTimelineComposable(navController)
                    }
                }
            }
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState,
                modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp)
                ) {
                    Column {
                        Spacer(modifier = Modifier.height(18.dp))

                        SheetItem(
                            header = stringResource(id = R.string.home),
                            description = stringResource(R.string.home_timeline_explained)
                        )

                        SheetItem(
                            header = stringResource(id = R.string.local),
                            description = stringResource(R.string.local_timeline_explained)
                        )

                        SheetItem(
                            header = stringResource(id = R.string.global),
                            description = stringResource(R.string.global_timeline_explained)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SheetItem(header: String, description: String) {
    Column {
        Text(text = header, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = description)
        Spacer(modifier = Modifier.height(16.dp))
    }
}