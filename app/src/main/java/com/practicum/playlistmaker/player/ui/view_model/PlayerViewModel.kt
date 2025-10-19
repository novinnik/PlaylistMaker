package com.practicum.playlistmaker.player.ui.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.player.ui.models.PlayerStatus
import com.practicum.playlistmaker.search.domain.models.timeConversion
import com.practicum.playlistmaker.util.Creator


class PlayerViewModel(private val trackUrl: String? =""): ViewModel() {

    companion object{

        private const val PLAY_DELAY = 500L
        private const val TIME_ZERO = 0L

        fun getFactory(trackUrl: String?): ViewModelProvider.Factory = viewModelFactory {
            initializer(){
                PlayerViewModel(trackUrl)
            }
        }
    }

    private val playerStateLiveData = MutableLiveData(PlayerStatus.DEFAULT)
    fun observePlayerState(): LiveData<PlayerStatus> = playerStateLiveData

    private val progressTimeLiveDate = MutableLiveData("00:00")
    fun observePlayerTimer(): LiveData<String> = progressTimeLiveDate

    private val playerInteractor = Creator.providePlayerInteractor()
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

        playerHandler.postDelayed(timerRunnable, TIME_ZERO)
    }

    private fun pauseTimer() {
        //останавливаем обновление текущего процесса, удаляем timerRunnable из handler
        playerHandler.removeCallbacks(timerRunnable)
    }

    private fun resetTimer() {
        //помимо остановки, сбрасываем таймер
        playerHandler.removeCallbacks(timerRunnable)
        progressTimeLiveDate.postValue(timeConversion(TIME_ZERO))
    }

    //работа плеера
    private fun playerPrepare(){
        trackUrl?.let{

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


}
