package com.practicum.playlistmaker.player.ui.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.player.domain.api.PlayerInteractor
import com.practicum.playlistmaker.player.ui.models.PlayerStatus
import com.practicum.playlistmaker.util.Converter.timeConversion

class PlayerViewModel(private val trackUrl: String,
                      private val playerInteractor: PlayerInteractor): ViewModel() {

    private val playerStateLiveData = MutableLiveData(PlayerStatus.DEFAULT)
    fun observePlayerState(): LiveData<PlayerStatus> = playerStateLiveData

    private val progressTimeLiveDate = MutableLiveData(TIME_ZERO)
    fun observePlayerTimer(): LiveData<String> = progressTimeLiveDate

    private val playerHandler = Handler(Looper.getMainLooper())

    private val timerRunnable =  Runnable {
        if (playerStateLiveData.value == PlayerStatus.PLAY) {
            startTimerUpdate()
        }
    }

    init {
        playerPrepare()
    }

    //работа таймера
    private fun startTimerUpdate() {
        val timeProgress  = timeConversion(playerInteractor.getCurrentPosition().toLong())
        progressTimeLiveDate.postValue(timeProgress)

        playerHandler.postDelayed(timerRunnable, PLAY_DELAY)
    }

    private fun pauseTimer() {
        //останавливаем обновление текущего процесса, удаляем timerRunnable из handler
        playerHandler.removeCallbacks(timerRunnable)
    }

    private fun resetTimer() {
        //помимо остановки, сбрасываем таймер
        playerHandler.removeCallbacks(timerRunnable)
        progressTimeLiveDate.postValue(TIME_ZERO)
    }

    //работа плеера
    private fun playerPrepare(){
        trackUrl.let{

            playerInteractor.playerPrepare(
                it,
                {
                    playerStateLiveData.postValue(PlayerStatus.PREPARED)
                },
                {
                    playerStateLiveData.postValue(PlayerStatus.PREPARED)
                    resetTimer()
                }
            )
        }
    }

    fun playerControl(){
        when (playerStateLiveData.value){
            PlayerStatus.PLAY -> playerPause()
            PlayerStatus.PAUSE, PlayerStatus.PREPARED -> playerStart()
            PlayerStatus.DEFAULT -> {}
            null -> TODO()
        }
    }

    private fun playerStart(){
        playerInteractor.playerStart()
        playerStateLiveData.postValue(PlayerStatus.PLAY)
        startTimerUpdate()
    }

    fun playerPause(){
        pauseTimer()
        playerInteractor.playerPause()
        playerStateLiveData.postValue(PlayerStatus.PAUSE)
    }

    override fun onCleared() {
        super.onCleared()
        playerInteractor.playerRelease()
        resetTimer()
    }

    companion object{
        private const val PLAY_DELAY = 500L
        private const val TIME_ZERO = "00:00"
    }
}
