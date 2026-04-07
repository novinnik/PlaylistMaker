package com.practicum.playlistmaker.setting.ui.activity

import SettingScreen
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.databinding.FragmentSettingsBinding
import com.practicum.playlistmaker.setting.ui.view_model.SettingViewModel
import com.practicum.playlistmaker.ui.theme.ThemeProject
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class SettingsFragment: Fragment() {

//    private var _binding : FragmentSettingsBinding? = null
//    private val binding get() = _binding!!
    private val viewModel by viewModel<SettingViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
//        return binding.root
        return ComposeView(requireContext()).apply {
            setContent {
                ThemeProject {
                    SettingScreen(viewModel = viewModel)
                }

            }
        }
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        viewModel.getDarkTheme().observe(viewLifecycleOwner){
//            binding.themeSwitch.isChecked = it
//        }
//
//        binding.settingShareApplication.setOnClickListener {
//            viewModel.shareApp()
//        }
//
//        binding.settingWriteSupport.setOnClickListener {
//            viewModel.writeSupport()
//        }
//
//        binding.settingUserAgreement.setOnClickListener {
//            viewModel.userAgreement()
//        }
//
//        binding.themeSwitch.setOnCheckedChangeListener { switcher, checked ->
//            viewModel.switchDarkTheme(checked)
//        }
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
}