package com.practicum.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.practicum.playlistmaker.App.Companion.PLAYLIST_MAKER_PREFERENCES
import com.practicum.playlistmaker.App.Companion.DARK_THEME


class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backButtonToolbar = findViewById<MaterialToolbar>(R.id.toolbar_activity_setting)
        backButtonToolbar.setNavigationOnClickListener { finish() }

        val shareApplication = findViewById<MaterialButton>(R.id.setting_share_application)
        shareApplication.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.setType("plain/text")
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.uri_android_developer))
            startActivity(Intent.createChooser(shareIntent, null))
        }

        val writeSupportButton = findViewById<MaterialButton>(R.id.setting_write_support)
        writeSupportButton.setOnClickListener {
            val supportIntent = Intent(Intent.ACTION_SENDTO)
            supportIntent.data = Uri.parse("mailto:")
            supportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("novinnik@yandex.ru"))
            supportIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.theme_support))
            supportIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.message_support))
            startActivity(supportIntent)
        }

        val userAgreementButton = findViewById<MaterialButton>(R.id.setting_user_agreement)
        userAgreementButton.setOnClickListener {
            val agreementIntent = Intent(Intent.ACTION_VIEW)
            agreementIntent.data = Uri.parse(getString(R.string.uri_yandex_practicum))
            startActivity(agreementIntent)
        }

        val themeSwitcher = findViewById<SwitchCompat>(R.id.theme_switch)

        val sharedPreferences = getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)

        themeSwitcher.isChecked = sharedPreferences.getBoolean(DARK_THEME, false)

        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            (applicationContext as App).switchTheme(checked)
        }

    }

}