package com.practicum.playlistmaker.searchResult


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ITunesService {

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL_ITUNES)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service = retrofit.create(ITunesApi::class.java)

    companion object {
        const val BASE_URL_ITUNES = "https://itunes.apple.com"
    }
}