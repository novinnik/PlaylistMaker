package com.practicum.playlistmaker.setting.ui.activity

import SettingScreen
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.setting.ui.view_model.SettingViewModel
import com.practicum.playlistmaker.ui.theme.ThemeProject
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class SettingsFragment: Fragment() {

    private val viewModel by viewModel<SettingViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                ThemeProject {
                    SettingScreen(viewModel = viewModel)
                }

            }
        }
    }

}