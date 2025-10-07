package com.practicum.playlistmaker.player.domain.impl

import android.media.MediaPlayer
import com.practicum.playlistmaker.player.domain.api.PlayerInteractor
import com.practicum.playlistmaker.player.domain.api.PlayerRepository

class PlayerInteractorImpl(private val playerRepository: PlayerRepository): PlayerInteractor {

    override fun playerPrepare(
        trackUrl: String,
        onPreparedListener: (MediaPlayer) -> Unit,
        onCompletionListener: (MediaPlayer) -> Unit
    ) {
        playerRepository.playerPrepare(
            trackUrl,
            onPreparedListener,
            onCompletionListener
        )
    }

    override fun playerStart() {
        playerRepository.playerStart()
    }

    override fun playerPause() {
        playerRepository.playerPause()
    }

    override fun playerRelease() {
        playerRepository.playerRelease()
    }

    override fun getCurrentPosition(): Int {
        return playerRepository.getCurrentPosition()
    }
}