package com.practicum.playlistmaker.setting.domain.api

import com.practicum.playlistmaker.setting.domain.models.ShareData

interface ShareInteractor {

    fun userAgreement(dataUrl: String)
    fun shareApp(dataUrl: String)
    fun writeSupport(emailData: ShareData)
}