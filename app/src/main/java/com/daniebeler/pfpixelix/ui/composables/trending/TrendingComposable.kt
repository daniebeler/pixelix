package com.daniebeler.pfpixelix.ui.composables.trending

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.QuestionMark
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.daniebeler.pfpixelix.R
import com.daniebeler.pfpixelix.ui.composables.SheetItem
import com.daniebeler.pfpixelix.ui.composables.trending.trending_accounts.TrendingAccountsComposable
import com.daniebeler.pfpixelix.ui.composables.trending.trending_hashtags.TrendingHashtagsComposable
import com.daniebeler.pfpixelix.ui.composables.trending.trending_posts.TrendingPostsComposable
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun TrendingComposable(navController: NavController, page: String) {

    val pageId = if (page == "accounts") 1 else if (page == "hashtags") 2 else 0
    val pagerState = rememberPagerState(initialPage = pageId, pageCount = { 3 })

    val scope = rememberCoroutineScope()

    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    var expanded by remember { mutableStateOf(false) }
    var range by remember { mutableStateOf("daily") }

    Scaffold(contentWindowInsets = WindowInsets(0.dp),
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(windowInsets = WindowInsets(0, 0, 0, 0), title = {
                Text(stringResource(R.string.trending), fontWeight = FontWeight.Bold)
            }, actions = {
                if (pagerState.currentPage == 0) {
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(imageVector = Icons.Outlined.DateRange, contentDescription = null)
                    }
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        DropdownMenuItem(text = { Text(stringResource(R.string.daily)) },
                            onClick = { range = "daily" },
                            trailingIcon = {
                                if (range == "daily") {
                                    Icon(
                                        imageVector = Icons.Outlined.Check,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            })
                        DropdownMenuItem(text = { Text(stringResource(R.string.monthly)) },
                            onClick = { range = "monthly" },
                            trailingIcon = {
                                if (range == "monthly") {
                                    Icon(
                                        imageVector = Icons.Outlined.Check,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            })
                        DropdownMenuItem(text = { Text(stringResource(R.string.yearly)) },
                            onClick = { range = "yearly" },
                            trailingIcon = {
                                if (range == "yearly") {
                                    Icon(
                                        imageVector = Icons.Outlined.Check,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            })
                    }
                }

                IconButton(onClick = { showBottomSheet = true }) {
                    Icon(imageVector = Icons.Outlined.QuestionMark, contentDescription = null)
                }
            })

        }) { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            PrimaryTabRow(selectedTabIndex = pagerState.currentPage) {
                Tab(text = { Text(stringResource(id = R.string.posts)) },
                    selected = pagerState.currentPage == 0,
                    selectedContentColor = MaterialTheme.colorScheme.primary,
                    unselectedContentColor = MaterialTheme.colorScheme.onBackground,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(0)
                        }

                    })

                Tab(text = { Text(stringResource(R.string.accounts)) },
                    selected = pagerState.currentPage == 1,
                    selectedContentColor = MaterialTheme.colorScheme.primary,
                    unselectedContentColor = MaterialTheme.colorScheme.onBackground,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(1)
                        }
                    })

                Tab(text = { Text(stringResource(R.string.hashtags)) },
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
                        TrendingPostsComposable(range, navController = navController)
                    }

                    1 -> Box(modifier = Modifier.fillMaxSize()) {
                        TrendingAccountsComposable(navController = navController)
                    }

                    2 -> Box(modifier = Modifier.fillMaxSize()) {
                        TrendingHashtagsComposable(navController = navController)
                    }

                }
            }
        }

        if (showBottomSheet) {
            ModalBottomSheet(onDismissRequest = {
                    showBottomSheet = false
                }, sheetState = sheetState,
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
                            header = stringResource(R.string.what_makes_a_post_trend),
                            description = stringResource(R.string.trending_post_description)
                        )

                        SheetItem(
                            header = stringResource(R.string.what_makes_an_account_trend),
                            description = stringResource(R.string.trending_account_description)
                        )

                        SheetItem(
                            header = stringResource(R.string.what_makes_a_hashtag_trend),
                            description = stringResource(R.string.trending_hashtag_description)
                        )
                    }
                }
            }
        }
    }
}