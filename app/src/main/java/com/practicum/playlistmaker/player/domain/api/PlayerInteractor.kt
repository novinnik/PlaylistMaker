package com.practicum.playlistmaker.player.domain.api

import android.media.MediaPlayer

interface PlayerInteractor {

    fun playerPrepare(trackUrl: String, onPreparedListener: (MediaPlayer) -> Unit, onCompletionListener: (MediaPlayer) -> Unit)
    fun playerStart()
    fun playerPause()
    fun playerRelease()
    fun getCurrentPosition(): Int
}