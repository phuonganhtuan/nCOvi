package com.example.ncovi.data.datasource.remote.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitBuilder {

    val apiService: ApiService = getRetrofit().create(ApiService::class.java)

    private const val BASE_URL = "https://api.apify.com/v2/"

    private fun getRetrofit() = run {
        val okHttpClient = OkHttpClient.Builder()
            .readTimeout(3000, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }
}
