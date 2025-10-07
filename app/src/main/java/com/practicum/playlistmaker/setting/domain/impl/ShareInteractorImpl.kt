package com.practicum.playlistmaker.setting.domain.impl

import com.practicum.playlistmaker.setting.domain.api.ShareInteractor
import com.practicum.playlistmaker.setting.domain.api.ShareRepository
import com.practicum.playlistmaker.setting.domain.models.ShareData

class ShareInteractorImpl(private val shareRepository: ShareRepository):ShareInteractor{
    override fun userAgreement(dataUrl: String) {
        return shareRepository.userAgreement(dataUrl)
    }

    override fun shareApp(dataUrl: String) {
        return shareRepository.shareApp(dataUrl)
    }

    override fun writeSupport(emailData: ShareData) {
        return shareRepository.writeSupport(emailData)
    }
}