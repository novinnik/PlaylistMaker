package com.practicum.playlistmaker.setting.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding
import com.practicum.playlistmaker.setting.ui.view_model.SettingViewModel

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private var viewModel: SettingViewModel?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, SettingViewModel.getFactory())
            .get(SettingViewModel::class.java)

        viewModel?.getDarkTheme()?.observe(this){
            binding.themeSwitch.isChecked = it
        }

        binding.toolbarActivitySetting.setNavigationOnClickListener { finish() }

        binding.settingShareApplication.setOnClickListener {
            viewModel?.shareApp()
        }

        binding.settingWriteSupport.setOnClickListener {
            viewModel?.writeSupport()
        }

        binding.settingUserAgreement.setOnClickListener {
            viewModel?.userAgreement()
        }

        binding.themeSwitch.setOnCheckedChangeListener { switcher, checked ->
            viewModel?.swithDarkTheme(checked)
        }

    }

}