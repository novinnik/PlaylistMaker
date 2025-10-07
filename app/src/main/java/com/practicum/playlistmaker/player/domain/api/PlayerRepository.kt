package com.practicum.playlistmaker.player.domain.api

interface PlayerRepository {
    fun playerPrepare(trackUrl: String, onPreparedListener: () -> Unit, onCompletionListener: () -> Unit)
    fun playerStart()
    fun playerPause()
    fun playerRelease()
    fun getCurrentPosition(): Int
}