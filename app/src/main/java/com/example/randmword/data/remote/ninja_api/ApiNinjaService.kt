package com.example.randmword.data.remote.ninja_api

import com.example.randmword.data.models.RandomWordResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers

interface ApiNinjaService {
    @GET("/v1/randomword")
    @Headers("X-Api-Key: PKsfSmndgsRzFWuPvlZCqw==haHxY0vSIGQsHdy4")
    suspend fun getRandomWord(): RandomWordResponse
}

fun createWordService(): ApiNinjaService{
    return Retrofit.Builder()
        .baseUrl("https://api.api-ninjas.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiNinjaService::class.java)
}