package com.practicum.playlistmaker.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL_ITUNES = "https://itunes.apple.com"

object RetrofitClient{

    //будет создаваться только при вызове
    private val retrofit : Retrofit by lazy{
        Retrofit.Builder()
            .baseUrl(BASE_URL_ITUNES)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun iTunesApiService(): ITunesApiService{
        return retrofit.create(ITunesApiService::class.java)
    }

}