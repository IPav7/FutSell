package com.example.futsell.ui.main

import android.util.Log
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Path
import java.util.concurrent.TimeUnit

object NetworkManager {

    private fun createOkHttpClient(errorHandlingInterceptor: IErrorHandlingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(errorHandlingInterceptor)
            .addInterceptor(getLogging())
            .connectTimeout(60L, TimeUnit.SECONDS)
            .readTimeout(60L, TimeUnit.SECONDS)
            .build()
    }

    private fun getLogging(): HttpLoggingInterceptor =
        HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
            Log.d("FUTSELL:", it)
        }).setLevel(HttpLoggingInterceptor.Level.BODY)

    private fun createRestApi(okHttpClient: OkHttpClient, url: String): NetworkApi {
        val gson = GsonBuilder()
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        return retrofit.create(NetworkApi::class.java)
    }

    suspend fun loadAvailablePlayer(id: String, timeStamp: String, sign: String): Response {
        return restApi.loadAvailablePlayer(id, timeStamp, sign)
    }

    private val restApi: NetworkApi

    init {
        restApi = createRestApi(createOkHttpClient(ErrorHandlingInterceptor()), "https://www.futsell.ru/")
    }

}