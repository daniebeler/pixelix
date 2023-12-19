package com.daniebeler.pixels.models.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory

class CountryRepositoryImpl: CountryRepository {

    private var BASE_URL = "https://pixelfed.social/"
    private var accessToken: String = ""

    private val pixelfedApi: PixelfedApi

    init {
        val mHttpLoggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)

        val mOkHttpClient = OkHttpClient
            .Builder()
            .addInterceptor(mHttpLoggingInterceptor)
            .build()

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(mOkHttpClient)
            .build()

        pixelfedApi = retrofit.create(PixelfedApi::class.java)
    }

    override fun setBaseUrl(baseUrl: String) {
        BASE_URL = baseUrl
    }

    override fun setAccessToken(token: String) {
        this.accessToken = "Bearer $token"
    }

    override suspend fun getTrendingPosts(range: String): List<Post> {
        return try {
            val response = pixelfedApi.getTrendingPosts(range).awaitResponse()
            if (response.isSuccessful) {
                val countries = response.body() ?: emptyList()
                countries.map { it.toModel() }
            } else {
                emptyList()
            }
        } catch (exception: Exception) {
            emptyList()
        }
    }

    override suspend fun getTrendingHashtags(): List<Hashtag> {
        return try {
            val response = pixelfedApi.getTrendingHashtags(accessToken).awaitResponse()
            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                emptyList()
            }
        } catch (exception: Exception) {
            emptyList()
        }
    }

    override suspend fun getLocalTimeline(): List<Post> {
        return try {
            val response = pixelfedApi.getLocalTimeline().awaitResponse()
            if (response.isSuccessful) {
                val countries = response.body() ?: emptyList()
                countries.map { it.toModel() }
            } else {
                emptyList()
            }
        } catch (exception: Exception) {
            emptyList()
        }
    }

    override suspend fun getHomeTimeline(accessToken: String): List<Post> {
        return try {
            val response = pixelfedApi.getHomeTimeline("Bearer $accessToken").awaitResponse()
            println(response)
            if (response.isSuccessful) {
                val countries = response.body() ?: emptyList()
                countries.map { it.toModel() }
            } else {
                emptyList()
            }
        } catch (exception: Exception) {
            println("foooof")
            println(exception)
            emptyList()
        }
    }

    override suspend fun getReplies(userid: String, postid: String): List<Reply> {
        return try {
            val response = pixelfedApi.getReplies(userid, postid).awaitResponse()
            if (response.isSuccessful) {
                val countries = response.body()?.data ?: emptyList()
                countries.map { it.toModel() }
            } else {
                emptyList()
            }
        } catch (exception: Exception) {
            emptyList()
        }
    }

    override suspend fun getAccount(accountId: String): Account {
        return try {
            val response = pixelfedApi.getAccount(accountId, accessToken).awaitResponse()
            if (response.isSuccessful) {
                val countries: Account = response.body() ?: Account("", "null", "null", "null",0, 0, "", "")
                countries
            } else {
                Account("", "null", "null", "null",0, 0, "", "")
            }
        } catch (exception: Exception) {
            Account("", "null", "null", "null",0, 0, "", "")
        }
    }

    override suspend fun getPostsByAccountId(accountId: String): List<Post> {
        return try {
            val response = pixelfedApi.getPostsByAccountId(accountId, accessToken).awaitResponse()
            if (response.isSuccessful) {
                val countries = response.body() ?: emptyList()
                countries.map { it.toModel() }
            } else {
                emptyList()
            }
        } catch (exception: Exception) {
            emptyList()
        }
    }


    override suspend fun getPostsByAccountId(accountId: String, maxPostId: String): List<Post> {
        return try {
            val response = pixelfedApi.getPostsByAccountId(accountId, accessToken, maxPostId).awaitResponse()
            if (response.isSuccessful) {
                val countries = response.body() ?: emptyList()
                countries.map { it.toModel() }
            } else {
                emptyList()
            }
        } catch (exception: Exception) {
            emptyList()
        }
    }

    // Does not work
    override suspend fun getPostById(postId: String): Post? {
        return try {
            val response = pixelfedApi.getPostById(postId).awaitResponse()
            if (response.isSuccessful) {
                val countries: Post? = response.body()
                countries
            } else {
                null
            }
        } catch (exception: Exception) {
            null
        }
    }


    // Auth

    override suspend fun createApplication(): Application? {
        return try {
            val response = pixelfedApi.createApplication().awaitResponse()
            if (response.isSuccessful) {
                val countries: Application? = response.body()
                countries
            } else {
                null
            }
        } catch (exception: Exception) {
            null
        }
    }

    override suspend fun obtainToken(clientId: String, clientSecret: String, code: String): AccessToken? {
        return try {
            println("im api call")
            println(clientId)
            println(clientSecret)
            println(code)
            val response = pixelfedApi.obtainToken(clientId, clientSecret, code).awaitResponse()
            if (response.isSuccessful) {
                println("uccess")
                println(response.body())
                val countries: AccessToken? = response.body()
                countries
            } else {
                println("not success")
                println(response)
                null
            }
        } catch (exception: Exception) {
            println("errorr")
            println(exception)
            null
        }
    }

    override suspend fun verifyToken(token: String): Account? {
        return try {
            println("im api call")
            val response = pixelfedApi.verifyToken("Bearer " + token).awaitResponse()
            if (response.isSuccessful) {
                println("uccess")
                println(response.body())
                val countries: Account? = response.body()
                countries
            } else {
                println("not success")
                println(response)
                null
            }
        } catch (exception: Exception) {
            println("errorr")
            println(exception)
            null
        }
    }

}