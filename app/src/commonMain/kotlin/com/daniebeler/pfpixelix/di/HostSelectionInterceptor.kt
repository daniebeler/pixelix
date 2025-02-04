import com.daniebeler.pfpixelix.di.HostSelectionInterceptorInterface
import io.ktor.client.call.HttpClientCall
import io.ktor.client.plugins.Sender
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.http.set
import kotlin.concurrent.Volatile

/** An interceptor that allows runtime changes to the URL hostname.  */
class HostSelectionInterceptor : HostSelectionInterceptorInterface {
    @Volatile
    private var host: String? = null

    @Volatile
    private var token: String? = null
    override fun setHost(host: String?) {
        this.host = host
    }

    override fun setToken(token: String?) {
        this.token = token
    }

    override suspend fun Sender.intercept(request: HttpRequestBuilder): HttpClientCall {
        if (request.url.toString().startsWith("https://err.or")) {
            request.apply {
                url.set(host = host)
                headers["Authorization"] = "Bearer $token"
            }
        }
        return execute(request)
    }
}