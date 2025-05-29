package com.creeper82.pozdroid.services.impl

import android.content.Context
import android.preference.PreferenceManager
import android.widget.Toast
import com.creeper82.pozdroid.SharedPrefUtils
import com.creeper82.pozdroid.services.abstraction.ApiInterface
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object PozNodeApiClient {
    private var instance: ApiInterface? = null
    private var currentBaseUrl: String? = null

    fun getApi(): ApiInterface {
        if (instance == null) refreshInstance(SharedPrefUtils.Defaults.SERVER_ADDRESS_DEFAULT)
        return instance!!
    }

    fun refreshInstance(baseUrl: String) {
        val url = if (!baseUrl.endsWith("/")) "$baseUrl/" else baseUrl

        instance = Retrofit.Builder()
            .baseUrl(url)
            .client(
                OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)

        currentBaseUrl = url
    }

    fun useFakeInstance() {
        instance = FakeApiClient()
        currentBaseUrl = "FAKE"
    }

    fun reloadBasedOnPrefs(context: Context) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)

        val useFakeData =
            prefs.getBoolean(
                SharedPrefUtils.USE_FAKE_DATA,
                SharedPrefUtils.Defaults.USE_FAKE_DATA_DEFAULT
            )

        if (useFakeData) {
            useFakeInstance()
        } else {

            val address =
                prefs.getString(
                    SharedPrefUtils.SERVER_ADDRESS,
                    SharedPrefUtils.Defaults.SERVER_ADDRESS_DEFAULT
                )!!

            try {
                refreshInstance(address)
            } catch (e: Exception) {
                Toast.makeText(
                    context,
                    "URL failure. Verify the server URL! Temporarily using localhost:5000/api",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}