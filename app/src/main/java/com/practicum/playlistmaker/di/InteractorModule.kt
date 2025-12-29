package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.media.favorites.domain.db.FavoritesInteractor
import com.practicum.playlistmaker.media.favorites.domain.impl.FavoritesInteractorImpl
import com.practicum.playlistmaker.player.domain.api.PlayerInteractor
import com.practicum.playlistmaker.player.domain.impl.PlayerInteractorImpl
import com.practicum.playlistmaker.search.domain.api.HistoryInteractor
import com.practicum.playlistmaker.search.domain.api.TracksSearchInteractor
import com.practicum.playlistmaker.search.domain.impl.HistoryInteractorImpl
import com.practicum.playlistmaker.search.domain.impl.TracksSearchInteractorImpl
import com.practicum.playlistmaker.setting.domain.api.ShareInteractor
import com.practicum.playlistmaker.setting.domain.api.ThemeInteractor
import com.practicum.playlistmaker.setting.domain.impl.ShareInteractorImpl
import com.practicum.playlistmaker.setting.domain.impl.ThemeInteractorImpl
import org.koin.dsl.module

val interactorModule = module {

    factory <PlayerInteractor>{
        PlayerInteractorImpl(get())
    }

    single <TracksSearchInteractor> {
        TracksSearchInteractorImpl(get())
    }

    single <HistoryInteractor> {
        HistoryInteractorImpl(get())
    }

    single <ShareInteractor> {
        ShareInteractorImpl(get())
    }

    single <ThemeInteractor> {
        ThemeInteractorImpl(get())
    }

    single <FavoritesInteractor>{
        FavoritesInteractorImpl(get())
    }
}
