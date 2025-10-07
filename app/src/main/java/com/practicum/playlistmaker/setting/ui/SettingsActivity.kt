package com.practicum.playlistmaker.setting.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.practicum.playlistmaker.Creator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.setting.domain.api.ShareInteractor
import com.practicum.playlistmaker.setting.domain.api.ThemeInteractor
import com.practicum.playlistmaker.setting.domain.models.ShareData


class SettingsActivity : AppCompatActivity() {

    private lateinit var themeInteractor: ThemeInteractor
    private lateinit var shareInteractor: ShareInteractor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        themeInteractor = Creator.provideThemeInteractor()
        shareInteractor = Creator.provideShareInteractor()

        val backButtonToolbar = findViewById<MaterialToolbar>(R.id.toolbar_activity_setting)
        backButtonToolbar.setNavigationOnClickListener { finish() }

        val shareApplication = findViewById<MaterialButton>(R.id.setting_share_application)
        shareApplication.setOnClickListener {
            shareInteractor.shareApp(getString(R.string.uri_android_developer))
        }

        val writeSupportButton = findViewById<MaterialButton>(R.id.setting_write_support)
        writeSupportButton.setOnClickListener {
            val shareData = ShareData(
                "novinnik@yandex.ru",
                getString(R.string.theme_support),
                getString(R.string.message_support)
            )
            shareInteractor.writeSupport(shareData)
        }

        val userAgreementButton = findViewById<MaterialButton>(R.id.setting_user_agreement)
        userAgreementButton.setOnClickListener {
            shareInteractor.userAgreement(getString(R.string.uri_yandex_practicum))
        }

        val themeSwitcher = findViewById<SwitchCompat>(R.id.theme_switch)

        themeSwitcher.isChecked = themeInteractor.isDarkTheme()

        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            themeInteractor.saveTheme(checked)
            themeInteractor.switchTheme(checked)
        }

    }

}