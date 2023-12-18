package com.daniebeler.pixels

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.daniebeler.pixels.ui.components.LoginComposable
import com.daniebeler.pixels.ui.theme.PixelsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            PixelsTheme {
                val navController: NavHostController = rememberNavController()

                Scaffold { paddingValues ->
                    Column (Modifier.padding(paddingValues)) {

                    }
                    LoginComposable(viewModel = mainViewModel, navController = navController)
                }
            }
        }
    }
}