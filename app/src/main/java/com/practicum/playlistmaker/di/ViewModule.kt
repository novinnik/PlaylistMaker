package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.media.favorites.ui.view_model.FavoritesViewModel
import com.practicum.playlistmaker.media.playlists.ui.view_model.PlaylistAddViewModel
import com.practicum.playlistmaker.media.playlists.ui.view_model.PlaylistInfoViewModel
import com.practicum.playlistmaker.media.playlists.ui.view_model.PlaylistsViewModel
import com.practicum.playlistmaker.player.ui.view_model.PlayerViewModel
import com.practicum.playlistmaker.search.ui.view_model.SearchViewModel
import com.practicum.playlistmaker.setting.ui.view_model.SettingViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModule = module {

    viewModel {(trackUrl:String) ->
        PlayerViewModel(trackUrl, get(), get(), get())
    }

    viewModel {
        SearchViewModel( get(), get())
    }

    viewModel {
        SettingViewModel(androidContext(), get(), get())
    }

    viewModel {
        FavoritesViewModel(get())
    }

    viewModel {
        PlaylistsViewModel(get())
    }

    viewModel {
        PlaylistAddViewModel(get(), get())
    }

    viewModel {(playlistId:Int) ->
        PlaylistInfoViewModel(playlistId,get(), get())
    }
}