package com.daniebeler.pixels.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.daniebeler.pixels.models.api.CountryRepository
import com.daniebeler.pixels.models.api.CountryRepositoryImpl
import com.daniebeler.pixels.models.api.Post
import com.daniebeler.pixels.models.api.PostDTO
import com.daniebeler.pixels.models.api.Reply
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SinglePostComposable(navController: NavController, postId: String) {

    println(postId)

    var post: Post? by remember {
        mutableStateOf(null)
    }

    val repository: CountryRepository = CountryRepositoryImpl()

    CoroutineScope(Dispatchers.Default).launch {
        post = repository.getPostById(postId)
    }

    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Single Post")
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
    ) {paddingValues ->
        Column (Modifier.padding(paddingValues)) {
            post?.let { PostComposable(it, navController) }
        }
    }
}