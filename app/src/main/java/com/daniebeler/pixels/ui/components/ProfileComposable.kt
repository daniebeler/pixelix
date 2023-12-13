package com.daniebeler.pixels.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.daniebeler.pixels.models.api.Account
import com.daniebeler.pixels.models.api.CountryRepository
import com.daniebeler.pixels.models.api.CountryRepositoryImpl
import com.daniebeler.pixels.models.api.Reply
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileComposable(navController: NavController, userId: String) {

    val uriHandler = LocalUriHandler.current

    var account: Account by remember {
        mutableStateOf(Account("null", "null", "null", "null", 0, 0, "", ""))
    }

    val repository: CountryRepository = CountryRepositoryImpl()

    CoroutineScope(Dispatchers.Default).launch {
        account = repository.getAccount(userId)
    }


    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text(text = account.username)
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
        if (account.id != "null") {
            Column (Modifier.padding(paddingValues)) {
                Column (Modifier.padding(12.dp)) {
                    Row (verticalAlignment = Alignment.CenterVertically) {
                        AsyncImage(
                            model = account.avatar,
                            contentDescription = "",
                            modifier = Modifier
                                .height(64.dp)
                                .clip(CircleShape))

                        Row (horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
                            Column (horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = account.followersCount.toString())
                                Text(text = "Followers")
                            }

                            Column (horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = account.followingCount.toString())
                                Text(text = "Following")
                            }
                        }



                    }
                    Text(text = account.displayname, fontWeight = FontWeight.Bold)
                    println("laaal")
                    println(account.toString())

                    account.note?.let {
                        Text(text = account.note)
                    }

                    account.website?.let {
                        Text(text = account.website, modifier = Modifier.clickable(onClick = { uriHandler.openUri(account.website)}))

                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(text = "Posts")
                }

            }
        }

    }
}