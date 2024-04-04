package com.daniebeler.pfpixelix.ui.composables.direct_messages.conversations

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ConversationsViewModel @Inject constructor(

): ViewModel() {

    var conversationsState by mutableStateOf(ConversationsState())



    fun refresh() {
        //getNotificationsFirstLoad(true)
    }
}