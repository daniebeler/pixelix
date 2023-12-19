package com.daniebeler.pixels.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.daniebeler.pixels.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrendingHashtagsComposable(viewModel: MainViewModel, navController: NavController) {

    Text(text = "Trending Hashtags")
}