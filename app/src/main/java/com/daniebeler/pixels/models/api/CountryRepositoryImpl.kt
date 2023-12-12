package com.daniebeler.pixels.models.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory

class CountryRepositoryImpl: CountryRepository {

    private val BASE_URL = "https://pixelfed.social/api/"

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

    override suspend fun getTrendingPosts(range: String): List<Post> {
        return try {
            val response = pixelfedApi.getTrendingPosts(range).awaitResponse()
            if (response.isSuccessful) {
                println("yay")
                println(response.body())
                val countries = response.body() ?: emptyList()
                countries.map { it.toModel() }
            } else {
                emptyList()
            }
        } catch (exception: Exception) {
            val e = exception
            println("fuck")
            println(e)
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
            val e = exception
            emptyList()
        }
    }

    override suspend fun getReplies(userid: String, postid: String): List<Reply> {
        println("saaasen")
        return try {
            val response = pixelfedApi.getReplies(userid, postid).awaitResponse()
            println("mehege")
            if (response.isSuccessful) {
                val countries = response.body()?.data ?: emptyList()
                countries.map { it.toModel() }
            } else {
                emptyList()
            }
        } catch (exception: Exception) {
            val e = exception
            println("Fuck")
            println(e)
            emptyList()
        }
    }

    override suspend fun getAccount(accountId: String): Account {
        return try {
            val response = pixelfedApi.getAccount(accountId).awaitResponse()
            println("mehege")
            if (response.isSuccessful) {
                val countries: Account = response.body() ?: Account("", "null", "null", "null",0, 0)
                countries
            } else {
                Account("", "null", "null", "null",0, 0)
            }
        } catch (exception: Exception) {
            val e = exception
            println("Fuck")
            println(e)
            Account("", "null", "null", "null",0, 0)
        }
    }
}