package com.practicum.playlistmaker.data.network


import com.practicum.playlistmaker.BASE_URL_ITUNES
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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