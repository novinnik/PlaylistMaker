package com.practicum.playlistmaker.setting.data.impl

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import com.practicum.playlistmaker.setting.domain.api.ShareRepository
import com.practicum.playlistmaker.setting.domain.models.ShareData

class ShareRepositoryImpl(private val context: Context): ShareRepository {

    override fun userAgreement(dataUrl: String) {
        val agreementIntent = Intent(Intent.ACTION_VIEW)
        agreementIntent.data = Uri.parse(dataUrl)
        agreementIntent.addFlags(FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(agreementIntent)
    }

    override fun shareApp(dataUrl: String) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.setType("plain/text")
        shareIntent.putExtra(Intent.EXTRA_TEXT, dataUrl)
        shareIntent.addFlags(FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(shareIntent)
    }

    override fun writeSupport(shareData: ShareData) {
        val supportIntent = Intent(Intent.ACTION_SENDTO)
        supportIntent.data = Uri.parse("mailto:")
        supportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(shareData.email))
        supportIntent.putExtra(Intent.EXTRA_SUBJECT, shareData.subject)
        supportIntent.putExtra(Intent.EXTRA_TEXT, shareData.text)
        supportIntent.addFlags(FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(supportIntent)
    }
}