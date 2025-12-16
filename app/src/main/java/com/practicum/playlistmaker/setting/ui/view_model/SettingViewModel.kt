package com.practicum.playlistmaker.setting.ui.view_model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.setting.domain.api.ShareInteractor
import com.practicum.playlistmaker.setting.domain.api.ThemeInteractor
import com.practicum.playlistmaker.setting.domain.models.ShareData

class SettingViewModel(private val context: Context,
                       private val themeInteractor: ThemeInteractor,
                       private val shareInteractor: ShareInteractor
    ):ViewModel() {

    private val darkThemeLiveData = MutableLiveData<Boolean>(themeInteractor.isDarkTheme())
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

    fun switchDarkTheme(isDark:Boolean){
        darkThemeLiveData.postValue(isDark)
        themeInteractor.switchTheme(isDark)
    }

}




