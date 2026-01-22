package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.media.favorites.domain.db.FavoritesInteractor
import com.practicum.playlistmaker.media.favorites.domain.impl.FavoritesInteractorImpl
import com.practicum.playlistmaker.media.playlists.domain.db.PlaylistsInteractor
import com.practicum.playlistmaker.media.playlists.domain.db.WorkingWithFilesInteractor
import com.practicum.playlistmaker.media.playlists.domain.impl.PlaylistsInteractorImpl
import com.practicum.playlistmaker.media.playlists.domain.impl.WorkingWithFileInteractorImpl
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

    factory <TracksSearchInteractor> {
        TracksSearchInteractorImpl(get())
    }

    factory <HistoryInteractor> {
        HistoryInteractorImpl(get())
    }

    factory <ShareInteractor> {
        ShareInteractorImpl(get())
    }

    factory <ThemeInteractor> {
        ThemeInteractorImpl(get())
    }

    factory <FavoritesInteractor>{
        FavoritesInteractorImpl(get())
    }

    factory <PlaylistsInteractor>{
        PlaylistsInteractorImpl(get())
    }

    factory <WorkingWithFilesInteractor>{
        WorkingWithFileInteractorImpl(get())
    }
}
