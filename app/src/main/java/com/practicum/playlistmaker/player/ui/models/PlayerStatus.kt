package com.practicum.playlistmaker.player.ui.models

import com.practicum.playlistmaker.search.domain.models.timeConversion

sealed class PlayerStatus(val timeProgress: String) {
    class Default: PlayerStatus(timeConversion(null))
    class Prepared: PlayerStatus(timeConversion(null))
    class Play(timeProgress: String): PlayerStatus(timeProgress)
    class Pause(timeProgress: String): PlayerStatus(timeProgress)
}