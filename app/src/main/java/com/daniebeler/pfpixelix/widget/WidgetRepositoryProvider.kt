package com.daniebeler.pfpixelix.widget

import HostSelectionInterceptor
import androidx.datastore.core.DataStore
import co.touchlab.kermit.Logger
import com.daniebeler.pfpixelix.data.remote.PixelfedApi
import com.daniebeler.pfpixelix.data.remote.createPixelfedApi
import com.daniebeler.pfpixelix.data.repository.WidgetRepositoryImpl
import com.daniebeler.pfpixelix.domain.model.AuthData
import com.daniebeler.pfpixelix.domain.model.LoginData
import com.daniebeler.pfpixelix.domain.repository.WidgetRepository
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.converter.CallConverterFactory
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.plugin
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json

class WidgetRepositoryProvider(private val dataStore: DataStore<AuthData>) {
    suspend operator fun invoke(): WidgetRepository? {
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


        val json = Json {
            ignoreUnknownKeys = true
            isLenient = true
        }

        val client = HttpClient {
            install(ContentNegotiation) { json(json) }
            install(Logging) {
                logger = object : io.ktor.client.plugins.logging.Logger {
                    override fun log(message: String) {
                        Logger.v("HttpClient") {
                            message.lines().joinToString { "\n\t\t$it" }
                        }
                    }
                }
                level = LogLevel.BODY
            }
        }.apply {
            plugin(HttpSend).intercept { request ->
                with(hostSelectionInterceptor) {
                    intercept(request)
                }
            }
        }

        val ktorfit = Ktorfit.Builder()
            .converterFactories(CallConverterFactory())
            .httpClient(client)
            .baseUrl("https://err.or/")
            .build()

        val service: PixelfedApi = ktorfit.createPixelfedApi()
        return WidgetRepositoryImpl(service)
    }

    private suspend fun getAuthData(): LoginData? {
        val currentlyLoggedIn = dataStore.data.first().currentlyLoggedIn
        return dataStore.data.first().loginDataList.find { it.accountId == currentlyLoggedIn }
    }
}