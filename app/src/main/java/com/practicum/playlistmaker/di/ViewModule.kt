package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.player.ui.view_model.PlayerViewModel
import com.practicum.playlistmaker.search.ui.view_model.SearchViewModel
import com.practicum.playlistmaker.setting.ui.view_model.SettingViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModule = module {

    viewModel {(trackUrl:String) ->
        PlayerViewModel(trackUrl, get())
    }

    viewModel {
        SearchViewModel( get(), get())
    }

    viewModel {
        SettingViewModel(androidContext(), get(), get())
    }
}