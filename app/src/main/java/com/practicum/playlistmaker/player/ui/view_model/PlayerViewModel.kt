package com.practicum.playlistmaker.player.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.media.favorites.domain.db.FavoritesInteractor
import com.practicum.playlistmaker.player.domain.api.PlayerInteractor
import com.practicum.playlistmaker.player.ui.models.PlayerStatus
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.util.Converter.timeConversion
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val trackUrl: String,
    private val playerInteractor: PlayerInteractor,
    private val favoritesInteractor: FavoritesInteractor
): ViewModel() {

    private var timerJob: Job? = null
    private val playerStateLiveData = MutableLiveData<PlayerStatus>(PlayerStatus.Default())
    fun observePlayerState(): LiveData<PlayerStatus> = playerStateLiveData

    private val isFavoriteLiveData = MutableLiveData(false)
    fun observeIsFavorite(): LiveData<Boolean> = isFavoriteLiveData

    init {
        playerPrepare()
    }

    //работа таймера
    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (isActive && playerInteractor.isPlaying()){
                delay(PLAY_DELAY)
                playerStateLiveData.postValue(PlayerStatus.Play(getTimeProgress()))
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    //работа плеера
    private fun playerPrepare(){
        trackUrl.let{
            playerInteractor.playerPrepare(
                it,
                {
                    playerStateLiveData.postValue(PlayerStatus.Prepared())
                },
                {
                    playerStateLiveData.postValue(PlayerStatus.Prepared())
                    stopTimer()
                }
            )
        }
    }

    fun playerControl(){
        when (playerStateLiveData.value){
            is PlayerStatus.Play -> playerPause()
            is PlayerStatus.Pause, is PlayerStatus.Prepared -> playerStart()
            else -> {}
        }
    }

    private fun playerStart(){
        playerInteractor.playerStart()
        playerStateLiveData.postValue(PlayerStatus.Play(getTimeProgress()))
        startTimer()
    }

    fun playerPause(){
        playerInteractor.playerPause()
        playerStateLiveData.postValue(PlayerStatus.Pause(getTimeProgress()))
        stopTimer()
    }

    override fun onCleared() {
        super.onCleared()
        playerInteractor.playerRelease()
        stopTimer()
    }

    private fun getTimeProgress(): String {
        return timeConversion(playerInteractor.getCurrentPosition().toLong())
    }

    fun onFavoriteClicked(track: Track){
        viewModelScope.launch {
            val isFavorite = track.isFavorite
            if (isFavorite){
                favoritesInteractor.deleteTrack(track)
            } else {
                favoritesInteractor.addTrack(track)
            }
            track.isFavorite = !isFavorite
            isFavoriteLiveData.postValue(!isFavorite)
        }
    }

    fun isFavoriteTrack(track: Track) {
        viewModelScope.launch {
            favoritesInteractor
                .isFavoritesById(track.id)
                .collectLatest { isFavorite ->
                    track.isFavorite = isFavorite
                    isFavoriteLiveData.postValue(isFavorite)
                }
        }
    }

    companion object{
        private const val PLAY_DELAY = 300L
    }
}
