package com.headway.bablicabdriver.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import kotlin.jvm.java

object RetrofitApiClient {

    private val interceptor = HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY)

    private val client = OkHttpClient.Builder()
        .connectTimeout(120, TimeUnit.SECONDS)
        .writeTimeout(120, TimeUnit.SECONDS)
        .readTimeout(120, TimeUnit.SECONDS)
        .addInterceptor(interceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL) // Replace with your API base URL
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiInterface : ApiInterface = retrofit.create(ApiInterface::class.java)

    private val googleRetrofit = Retrofit.Builder()
        .baseUrl(GOOGLE_APIS_BASE_URL) // Replace with your API base URL
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val googleApiInterface : ApiInterface = googleRetrofit.create(ApiInterface::class.java)


    private val googleRouteRetrofit = Retrofit.Builder()
        .baseUrl(GOOGLE_ROUTES_BASE_URL) // Replace with your API base URL
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val googleRouteApiInterface : ApiInterface = googleRouteRetrofit.create(ApiInterface::class.java)
}