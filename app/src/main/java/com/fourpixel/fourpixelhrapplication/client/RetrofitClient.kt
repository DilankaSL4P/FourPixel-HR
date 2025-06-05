package com.fourpixel.fourpixelhrapplication.client

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object RetrofitClient {
    private const val BASE_URL = "https://demohr.bsuite.space/public/"

    val instance: Retrofit by lazy {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // This logs headers and body
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor) // <--- ADD THIS LINE
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient) // <--- ENSURE THIS IS USING YOUR okHttpClient
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
