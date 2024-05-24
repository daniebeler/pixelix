package com.daniebeler.pfpixelix.widget.utils

import HostSelectionInterceptor
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.daniebeler.pfpixelix.common.Constants
import com.daniebeler.pfpixelix.data.remote.PixelfedApi
import com.daniebeler.pfpixelix.data.repository.WidgetRepositoryImpl
import com.daniebeler.pfpixelix.domain.repository.WidgetRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WidgetRepositoryProvider(private val storage: DataStore<Preferences>) {
    suspend operator fun invoke(): WidgetRepository {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val hostSelectionInterceptor = HostSelectionInterceptor()
        hostSelectionInterceptor.setHost(getBaseUrl().first().replace("https://", ""))
        hostSelectionInterceptor.setToken(
            getToken().first()
        )
        val client = OkHttpClient.Builder().addInterceptor(hostSelectionInterceptor)
            .addInterceptor(logging).build()

        val retrofit: Retrofit = Retrofit.Builder().addConverterFactory(
            GsonConverterFactory.create()
        ).client(client).baseUrl("https://err.or/").build()

        val service = retrofit.create(PixelfedApi::class.java)
        return WidgetRepositoryImpl(service)
    }

    private fun getBaseUrl() = storage.data.map { preferences ->
        preferences[stringPreferencesKey(Constants.BASE_URL_DATASTORE_KEY)] ?: ""
    }

    private fun getToken() = storage.data.map { preferences ->
        preferences[stringPreferencesKey(Constants.ACCESS_TOKEN_DATASTORE_KEY)] ?: ""
    }
}