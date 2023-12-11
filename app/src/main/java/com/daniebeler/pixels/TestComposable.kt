package com.daniebeler.pixels

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import com.daniebeler.pixels.MainViewModel

@Composable
fun TestComposable(viewModel: MainViewModel) {

    val items = viewModel.countries

    Column {
        Column {
            Text(text = "Meeeeem")
            Text(text = "Meeeeem")
            Text(text = "Meeeeem")
            Text(text = "Meeeeem")
            Button(onClick = { viewModel.searchCountries("austria") }) {

            }

            Text(text = items.toString())

            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items) { item ->
                    Text(text = "Meeeeem")
                }
            }
        }
    }
}