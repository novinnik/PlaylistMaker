package com.practicum.playlistmaker.player.domain.api

interface PlayerInteractor {
    fun playerPrepare(trackUrl: String, onPreparedListener: () -> Unit, onCompletionListener: () -> Unit)
    fun playerStart()
    fun playerPause()
    fun playerRelease()
    fun getCurrentPosition(): Int
    fun isPlaying(): Boolean
}