package com.practicum.playlistmaker

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.practicum.playlistmaker.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.player.data.impl.PlayerRepositoryImpl
import com.practicum.playlistmaker.player.domain.api.PlayerInteractor
import com.practicum.playlistmaker.player.domain.impl.PlayerInteractorImpl
import com.practicum.playlistmaker.search.data.impl.HistoryRepositoryImpl
import com.practicum.playlistmaker.search.data.impl.TracksSearchRepositoryImpl
import com.practicum.playlistmaker.search.domain.api.HistoryInteractor
import com.practicum.playlistmaker.search.domain.api.HistoryRepository
import com.practicum.playlistmaker.search.domain.api.TracksSearchInteractor
import com.practicum.playlistmaker.search.domain.api.TracksSearchRepository
import com.practicum.playlistmaker.search.domain.impl.HistoryInteractorImpl
import com.practicum.playlistmaker.search.domain.impl.TracksSearchInteractorImpl
import com.practicum.playlistmaker.setting.data.impl.ShareRepositoryImpl
import com.practicum.playlistmaker.setting.data.impl.ThemeRepositoryImpl
import com.practicum.playlistmaker.setting.domain.api.ShareInteractor
import com.practicum.playlistmaker.setting.domain.api.ShareRepository
import com.practicum.playlistmaker.setting.domain.api.ThemeInteractor
import com.practicum.playlistmaker.setting.domain.api.ThemeRepository
import com.practicum.playlistmaker.setting.domain.impl.ShareInteractorImpl
import com.practicum.playlistmaker.setting.domain.impl.ThemeInteractorImpl


object Creator {
    private lateinit var application: Application
    private lateinit var context: Context

    fun initApplication(application: Application){
        this.application = application
        context = application.applicationContext
    }

    fun provideSharedPreferences(): SharedPreferences{
        val SEARCH_HISTORY_PREF = "search_history_pref"
        return application.getSharedPreferences(SEARCH_HISTORY_PREF, Context.MODE_PRIVATE )
    }

    //tracks
    fun getTracksRepository(): TracksSearchRepository{
        return TracksSearchRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTrackInteractor(): TracksSearchInteractor {
        return TracksSearchInteractorImpl(getTracksRepository())
    }
    //history
    private fun getHistoryRepository(): HistoryRepository {
        return HistoryRepositoryImpl()
    }
    fun provideHistoryInteractor(): HistoryInteractor {
        return HistoryInteractorImpl(getHistoryRepository())
    }

    //player
//    private fun getPlayerRepository(): PlayerRepository {
//        return PlayerRepositoryImpl()
//    }

    fun providePlayerInteractor(): PlayerInteractor {
        return PlayerInteractorImpl(PlayerRepositoryImpl())
    }

    //theme
    private fun getThemeRepository(): ThemeRepository {
         return ThemeRepositoryImpl(provideSharedPreferences())
    }

    fun provideThemeInteractor(): ThemeInteractor{
        return ThemeInteractorImpl(getThemeRepository())
    }

    //setting
    //theme
    private fun getShareRepository(): ShareRepository {
        return ShareRepositoryImpl(context)
    }

    fun provideShareInteractor(): ShareInteractor{
        return ShareInteractorImpl(getShareRepository())
    }

}