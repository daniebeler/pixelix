package com.daniebeler.pixelix.ui.composables.newpost

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.daniebeler.pixelix.domain.repository.CountryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewPostViewModel @Inject constructor(
    private val repository: CountryRepository
) : ViewModel() {
    var uris: List<Uri> = emptyList()
    var caption: String by mutableStateOf("")
    var altText: String by mutableStateOf("")
    var sensitive: Boolean by mutableStateOf(false)
    var sensitiveText: String by mutableStateOf("")
    var audience: String by mutableStateOf("public")

    fun post() {
        uris.forEach{
            
        }
    }
}