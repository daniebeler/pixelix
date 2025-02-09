package com.daniebeler.pfpixelix.domain.service.session

import androidx.datastore.core.DataStore
import co.touchlab.kermit.Logger
import com.daniebeler.pfpixelix.di.AppSingleton
import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.URLBuilder
import io.ktor.http.Url
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Inject

@Inject
@AppSingleton
class AuthService(
    private val urlHandler: SystemUrlHandler,
    private val session: Session,
    private val sessionStorage: DataStore<SessionStorage>,
    private val json: Json
) {
    companion object {
        private const val clientName = "pixelix"
        private const val grantType = "authorization_code"
        private const val redirectUrl = "pixelix-android-auth://callback"
        private val domainRegex: Regex =
            "^((\\*)|((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)|((\\*\\.)?([a-zA-Z0-9-]+\\.){0,5}[a-zA-Z0-9-][a-zA-Z0-9-]+\\.[a-zA-Z]{2,63}?))\$".toRegex()
    }

    val activeUser: Flow<String?> = session.credentials.map { it?.accountId }

    suspend fun auth(host: String) {
        Logger.d("new authorization")
        val serverUrl = getServerUrl(host)
        val api = createAuthApi(serverUrl)
        val authData = api.getAuthData(clientName, redirectUrl)

        val authUrl = URLBuilder("${serverUrl}oauth/authorize").apply {
            parameters.apply {
                append("response_type", "code")
                append("redirect_uri", redirectUrl)
                append("client_id", authData.clientId)
            }
        }.build()

        Logger.d("open oauth url")
        urlHandler.openBrowser(authUrl.toString())

        Logger.d("wait oauth redirect")
        val redirect = Url(urlHandler.redirects.first())
        Logger.d("handle oauth redirect")

        val code = redirect.parameters["code"] ?: error("Redirect doesn't have a code")

        val token = api.getToken(
            authData.clientId,
            authData.clientSecret,
            code,
            redirectUrl,
            grantType
        )

        val account = api.verify("Bearer ${token.accessToken}")

        val newCred = Credentials(
            accountId = requireNotNull(account.id),
            username = requireNotNull(account.username),
            displayName = account.displayName ?: account.username,
            avatar = account.avatar.orEmpty(),
            serverUrl = serverUrl.toString(),
            token = token.accessToken
        )
        sessionStorage.updateData { data ->
            data.copy(
                sessions = data.sessions + newCred,
                activeUserId = newCred.accountId
            )
        }
        session.setCredentials(newCred)
    }

    suspend fun openSessionIfExist(userId: String? = null) {
        sessionStorage.updateData { data ->
            val cred = if (userId == null) {
                data.getActiveSession()
            } else {
                data.sessions.firstOrNull { it.accountId == userId }
            }
            session.setCredentials(cred)
            data.copy(activeUserId = cred?.accountId)
        }
    }

    fun isValidHost(host: String): Boolean = domainRegex.matches(host)

    suspend fun deleteSession(userId: String? = null) {
        sessionStorage.updateData { data ->
            val id = userId ?: data.activeUserId
            val newSessions = data.sessions.filter { it.accountId != id }.toSet()
            val newId = if (data.activeUserId == id) {
                newSessions.firstOrNull()?.accountId
            } else {
                data.activeUserId
            }
            data.copy(sessions = newSessions, activeUserId = newId)
        }
        if (userId == null) {
            openSessionIfExist()
        }
    }

    suspend fun getAvailableSessions(): SessionStorage {
        return sessionStorage.data.first()
    }

    fun getCurrentSession(): Credentials? {
        return session.credentials.value
    }

    private fun getServerUrl(host: String): Url {
        require(isValidHost(host)) { "The host is invalid '$host'" }
        return Url("https://$host/")
    }

    private fun createAuthApi(baseUrl: Url): AuthApi {
        val httpClient = HttpClient {
            install(ContentNegotiation) { json(json) }
            install(Logging) {
                logger = object : io.ktor.client.plugins.logging.Logger {
                    override fun log(message: String) {
                        Logger.v("HttpAuth") {
                            message.lines().joinToString { "\n\t\t$it" }
                        }
                    }
                }
                level = LogLevel.INFO
            }
        }
        val ktorfit = Ktorfit.Builder()
            .httpClient(httpClient)
            .baseUrl(baseUrl.toString())
            .build()
        return ktorfit.createAuthApi()
    }
}
