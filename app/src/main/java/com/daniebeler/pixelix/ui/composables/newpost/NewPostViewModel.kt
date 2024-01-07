package com.daniebeler.pixelix.ui.composables.newpost

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.daniebeler.pixelix.domain.repository.CountryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewPostViewModel@Inject constructor(
    private val repository: CountryRepository
): ViewModel() {
    var uris: List<Uri> = emptyList()
}