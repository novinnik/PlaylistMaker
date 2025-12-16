package com.practicum.playlistmaker.di

import android.content.Context
import android.media.MediaPlayer
import com.google.gson.Gson
import com.practicum.playlistmaker.data.NetworkClient
import com.practicum.playlistmaker.data.network.ITunesApiService
import com.practicum.playlistmaker.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.setting.data.storage.ThemePrefStorage
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module{

    single <ITunesApiService> {
        Retrofit.Builder()
            .baseUrl(BASE_URL_ITUNES)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesApiService::class.java)
    }

    single { androidContext().getSharedPreferences(SEARCH_HISTORY_PREF, Context.MODE_PRIVATE ) }

    factory { Gson() }

    single<NetworkClient> { RetrofitNetworkClient(get()) }

    factory { MediaPlayer() }

    single { ThemePrefStorage(androidContext(), get())}
}

private const val BASE_URL_ITUNES = "https://itunes.apple.com"
private const val SEARCH_HISTORY_PREF = "search_history_pref"