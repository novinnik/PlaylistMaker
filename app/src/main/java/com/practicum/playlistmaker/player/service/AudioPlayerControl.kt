package com.practicum.playlistmaker.player.service

import com.practicum.playlistmaker.player.ui.models.PlayerStatus
import kotlinx.coroutines.flow.StateFlow

interface AudioPlayerControl {
    fun getPlayerState(): StateFlow<PlayerStatus>
    fun playerStart()
    fun playerPause()
    fun showNotification()
    fun hideNotification()
}