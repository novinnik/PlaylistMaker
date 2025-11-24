package com.practicum.playlistmaker.setting.data.impl

import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.setting.domain.api.ThemeRepository
import com.practicum.playlistmaker.Resource
import com.practicum.playlistmaker.setting.data.storage.ThemePrefStorage

class ThemeRepositoryImpl(private val storage: ThemePrefStorage) : ThemeRepository{

    override fun isDarkTheme(): Resource<Boolean> {
        return Resource.Success(storage.getData())
    }

    override fun switchTheme(darkTheme: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkTheme){
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        storage.storeData(darkTheme)
    }

}