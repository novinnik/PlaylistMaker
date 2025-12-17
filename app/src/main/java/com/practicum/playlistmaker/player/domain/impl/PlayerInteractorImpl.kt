package com.practicum.playlistmaker.player.domain.impl

import com.practicum.playlistmaker.player.domain.api.PlayerInteractor
import com.practicum.playlistmaker.player.domain.api.PlayerRepository

class PlayerInteractorImpl(private val playerRepository: PlayerRepository): PlayerInteractor {

    override fun playerPrepare(
        trackUrl: String,
        onPreparedListener: () -> Unit,
        onCompletionListener: () -> Unit
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

    override fun isPlaying(): Boolean {
        return playerRepository.isPlaying()
    }
}