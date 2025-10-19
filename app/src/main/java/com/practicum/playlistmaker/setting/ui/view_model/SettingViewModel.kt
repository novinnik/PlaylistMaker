package com.practicum.playlistmaker.setting.ui.view_model

import android.app.Application

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.setting.domain.api.ShareInteractor
import com.practicum.playlistmaker.setting.domain.api.ThemeInteractor
import com.practicum.playlistmaker.setting.domain.models.ShareData
import com.practicum.playlistmaker.util.Creator

class SettingViewModel(private val context: Context):ViewModel() {

    private val themeInteractor: ThemeInteractor = Creator.provideThemeInteractor()
    private val shareInteractor: ShareInteractor = Creator.provideShareInteractor()

    companion object{
        fun getFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[APPLICATION_KEY] as Application)
                SettingViewModel(app)
            }
        }
    }
    private val darktheme = themeInteractor.isDarkTheme()?:false

    private val darkThemeLiveData = MutableLiveData<Boolean>()
    fun getDarkTheme(): LiveData<Boolean> = darkThemeLiveData

    fun shareApp(){
        shareInteractor.shareApp(context.getString(R.string.uri_android_developer))
    }

    fun userAgreement(){
        shareInteractor.userAgreement(context.getString(R.string.uri_yandex_practicum))
    }

    fun writeSupport(){
        val shareData = ShareData(
            context.getString(R.string.email_support),
            context.getString(R.string.theme_support),
            context.getString(R.string.message_support)
        )
        shareInteractor.writeSupport(shareData)
    }

    fun swithDarkTheme(isDark:Boolean){
        themeInteractor.saveTheme(isDark)
        themeInteractor.switchTheme(isDark)
        darkThemeLiveData.postValue(isDark)
    }

}




