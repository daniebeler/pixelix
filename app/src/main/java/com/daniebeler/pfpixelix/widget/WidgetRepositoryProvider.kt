package com.daniebeler.pfpixelix.widget

import HostSelectionInterceptor
import androidx.datastore.core.DataStore
import com.daniebeler.pfpixelix.data.remote.PixelfedApi
import com.daniebeler.pfpixelix.data.repository.WidgetRepositoryImpl
import com.daniebeler.pfpixelix.domain.model.AuthData
import com.daniebeler.pfpixelix.domain.model.LoginData
import com.daniebeler.pfpixelix.domain.repository.WidgetRepository
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

class WidgetRepositoryProvider(private val dataStore: DataStore<AuthData>) {
    suspend operator fun invoke(): WidgetRepository? {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val hostSelectionInterceptor = HostSelectionInterceptor()
        val loginData: LoginData? = getAuthData()
        if (loginData == null) {
            return null
        }
        val baseUrl = loginData.baseUrl
        val jwtToken = loginData.accessToken
        if (baseUrl.isBlank() || jwtToken.isBlank()) {
            return null
        }
        hostSelectionInterceptor.setHost(baseUrl.replace("https://", ""))
        hostSelectionInterceptor.setToken(
            jwtToken
        )
        val client = OkHttpClient.Builder().addInterceptor(hostSelectionInterceptor)
            .addInterceptor(logging).build()

        val json = Json {
            ignoreUnknownKeys = true
            isLenient = true
        }

        val retrofit: Retrofit = Retrofit.Builder().addConverterFactory(
            json.asConverterFactory("application/json; charset=UTF8".toMediaType())
        ).client(client).baseUrl("https://err.or/").build()

        val service = retrofit.create(PixelfedApi::class.java)
        return WidgetRepositoryImpl(service)
    }

    private suspend fun getAuthData(): LoginData? {
        val currentlyLoggedIn = dataStore.data.first().currentlyLoggedIn
        return dataStore.data.first().loginDataList.find { it.accountId == currentlyLoggedIn }
    }
}