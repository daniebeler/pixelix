import com.daniebeler.pfpixelix.di.HostSelectionInterceptorInterface
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

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

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request: Request = chain.request()
        val host = host
        if (request.url.toString().startsWith("https://err.or")) {
            if (host != null) {
                val newUrl = request.url.newBuilder().host(host).build()
                request = request.newBuilder().url(newUrl).build()
            }

            val token = token
            if (token != null) {
                request = request.newBuilder().addHeader("Authorization", "Bearer $token").build()
            }
        }

        return chain.proceed(request)
    }
}