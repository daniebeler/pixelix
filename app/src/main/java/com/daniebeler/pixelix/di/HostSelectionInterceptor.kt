import com.daniebeler.pixelix.di.HostSelectionInterceptorInterface
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
        if (host != null) {
            val newUrl = request.url.newBuilder().host(host).build()
            request = request.newBuilder().url(newUrl).build()
        }

        val token = token
        if (token != null) {
            request = request.newBuilder().addHeader("Authorization", "Bearer $token").build()
        }
        return chain.proceed(request)
    }

    companion object {
        @Throws(Exception::class)
        @JvmStatic
        fun main(args: Array<String>) {
            val interceptor = HostSelectionInterceptor()
            val okHttpClient: OkHttpClient =
                OkHttpClient.Builder().addInterceptor(interceptor).build()
            val request: Request =
                Request.Builder().url("http://www.coca-cola.com/robots.txt").build()
            val call1 = okHttpClient.newCall(request)
            val response1 = call1.execute()
            println("RESPONSE FROM: " + response1.request.url)
            println(response1.body!!.string())
            interceptor.setHost("www.pepsi.com")
            val call2 = okHttpClient.newCall(request)
            val response2 = call2.execute()
            println("RESPONSE FROM: " + response2.request.url)
            println(response2.body!!.string())
        }
    }
}