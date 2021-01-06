package com.example.futsell.ui.main

import retrofit2.http.GET
import retrofit2.http.Path

interface NetworkApi {

    @GET("ffa19/api/pop/id/{id}/ts/{ts}/sign/{sign}/sku/FFA19PS4/")
    suspend fun loadAvailablePlayer(
        @Path("id") id: String,
        @Path("ts") timeStamp: String,
        @Path("sign") sign: String
    ): Response

}