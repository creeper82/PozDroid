package com.creeper82.pozdroid.services.impl

import com.creeper82.pozdroid.PrefKeys
import com.creeper82.pozdroid.services.abstraction.ApiInterface
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object PozNodeApiClient {
    private var retrofit: ApiInterface? = null
    private var currentBaseUrl: String? = null

    fun getApi(): ApiInterface {
        if (retrofit == null) refreshInstance(PrefKeys.Defaults.SERVER_ADDRESS_DEFAULT)
        return retrofit!!
    }

    fun refreshInstance(baseUrl: String) {
        val url = if (!baseUrl.endsWith("/")) "$baseUrl/" else baseUrl

        retrofit = Retrofit.Builder()
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
}