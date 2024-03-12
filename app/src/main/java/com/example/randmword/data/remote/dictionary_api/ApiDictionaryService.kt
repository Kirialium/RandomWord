package com.example.randmword.data.remote.dictionary_api

import com.example.randmword.data.models.DictionaryResponse
import com.example.randmword.data.models.DictionaryResponseFull
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiDictionaryService {
    @GET("entries/en/{word}")
    suspend fun getDefinition(@Path("word")word: String): DictionaryResponseFull
}

fun createDictionaryService(): ApiDictionaryService{
    return Retrofit.Builder()
        .baseUrl("https://api.dictionaryapi.dev/api/v2/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiDictionaryService::class.java)
}