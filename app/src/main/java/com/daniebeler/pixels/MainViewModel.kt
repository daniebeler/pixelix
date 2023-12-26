package com.daniebeler.pixels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.daniebeler.pixels.api.CountryRepository
import com.daniebeler.pixels.domain.model.Account
import com.daniebeler.pixels.domain.model.Application
import com.daniebeler.pixels.domain.model.Post
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: CountryRepository,
    application: android.app.Application
): AndroidViewModel(application) {

    var ownAccount: Account? by mutableStateOf(null)
    var ownPosts: List<Post> by mutableStateOf(emptyList())

    var _authApplication: Application? = null

    fun getOwnAccount() {
        viewModelScope.launch {
            ownAccount = repository.getAccount("497910174831013185")
        }
    }

    fun getOwnPosts() {
        viewModelScope.launch {
            ownPosts = repository.getPostsByAccountId("497910174831013185")
        }
    }

    fun getMoreOwnPosts(maxPostId: String) {
        viewModelScope.launch {
            ownPosts += repository.getPostsByAccountId("497910174831013185", maxPostId)
        }
    }

    suspend fun registerApplication(): String {
        _authApplication = repository.createApplication()
        if (_authApplication != null) {
            storeClientId(_authApplication!!.clientId)
            storeClientSecret(_authApplication!!.clientSecret)
            return _authApplication!!.clientId
        }
        return ""
    }

    suspend fun obtainToken(code: String): Boolean {
        val clientId: String = getClientIdFromStorage().first()
        println("obtainToken: clientId")
        println(clientId)
        val clientSecret: String = getClientSecretFromStorage().first()

        val token = repository.obtainToken(clientId, clientSecret, code)

        if (token != null) {
            storeAccessToken(token.accessToken)
            return true
        }

        return false
    }

    fun doesTokenExist(): Boolean {
        return repository.doesAccessTokenExist()
    }


    suspend fun storeClientId(clientId: String) {
        repository.storeClientId(clientId)
    }

    fun getClientIdFromStorage(): Flow<String> {
        return repository.getClientIdFromStorage()
    }

    suspend fun storeClientSecret(clientSecret: String) {
        repository.storeClientSecret(clientSecret)
    }

    fun getClientSecretFromStorage(): Flow<String> {
        return repository.getClientSecretFromStorage()
    }

    suspend fun storeAccessToken(accessToken: String) {
        repository.storeAccessToken(accessToken)
    }

    suspend fun logout() {
        repository.setAccessToken("")
        storeClientId("")
        storeClientSecret("")

    }

}