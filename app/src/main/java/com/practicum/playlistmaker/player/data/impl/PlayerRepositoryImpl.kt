package com.practicum.playlistmaker.player.data.impl

import android.media.MediaPlayer
import com.practicum.playlistmaker.player.domain.api.PlayerRepository

class PlayerRepositoryImpl: PlayerRepository{

    private val player = MediaPlayer()

    override fun playerPrepare(
        trackUrl: String,
        onPreparedListener: (MediaPlayer) -> Unit,
        onCompletionListener: (MediaPlayer) -> Unit
    ) {
            player.setDataSource(trackUrl)
            player.prepareAsync()
            player.setOnPreparedListener (onPreparedListener)
            player.setOnCompletionListener (onCompletionListener)
    }

    override fun playerStart() {
        player.start()
    }

    override fun playerPause() {
        player.pause()
    }

    override fun playerRelease() {
        player.release()
    }

    override fun getCurrentPosition(): Int {
        return player.currentPosition;
    }


}