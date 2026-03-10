package com.practicum.playlistmaker.player.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.media.favorites.domain.db.FavoritesInteractor
import com.practicum.playlistmaker.media.playlists.domain.db.PlaylistsInteractor
import com.practicum.playlistmaker.media.playlists.domain.model.Playlist
import com.practicum.playlistmaker.media.playlists.model.PlaylistState
import com.practicum.playlistmaker.player.service.AudioPlayerControl
import com.practicum.playlistmaker.player.ui.models.PlayerStatus
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class PlayerViewModel(
    private val trackUrl: String,
    private val favoritesInteractor: FavoritesInteractor,
    private val playlistsInteractor: PlaylistsInteractor
): ViewModel() {

    private val playerStateLiveData = MutableLiveData<PlayerStatus>(PlayerStatus.Default())
    fun observePlayerState(): LiveData<PlayerStatus> = playerStateLiveData

    private val isFavoriteLiveData = MutableLiveData(false)
    fun observeIsFavorite(): LiveData<Boolean> = isFavoriteLiveData

    private val playlistsStateLiveData = MutableLiveData< PlaylistState>(PlaylistState.Loading)
    fun observePlaylistsState(): LiveData<PlaylistState> = playlistsStateLiveData

    private var audioPlayerControl: AudioPlayerControl? = null

    init {
        getPlaylists()
    }

    fun setAudioPlayerControl(audioPlayerControl: AudioPlayerControl){
        this.audioPlayerControl = audioPlayerControl
        viewModelScope.launch {
            audioPlayerControl.getPlayerState().collect {
                playerStateLiveData.postValue(it)
            }
        }
    }

    fun showNotification(){
        if (playerStateLiveData.value is PlayerStatus.Play) {
            audioPlayerControl?.showNotification()
        }
    }
    fun hideNotification(){
        if (playerStateLiveData.value is PlayerStatus.Play) {
            audioPlayerControl?.hideNotification()
        }
    }

    fun removeAudioPlayerControl(){
        audioPlayerControl = null
    }

    fun playerControl(){
        when (playerStateLiveData.value){
            is PlayerStatus.Play -> audioPlayerControl?.playerPause()
            else -> audioPlayerControl?.playerStart()
        }
    }


    override fun onCleared() {
        super.onCleared()
        removeAudioPlayerControl()
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

    //плейлисты
    fun getPlaylists(){
       playlistsStateLiveData.postValue(PlaylistState.Loading)
        viewModelScope.launch {
            playlistsInteractor
                .getPlaylists()
                .collect { value ->
                    processResultPlaylists(value)
                }
        }
    }

    private fun processResultPlaylists(playlist: List<Playlist>){
        if (playlist.isNotEmpty()){
            renderStatePlaylists(PlaylistState.Content(playlist))
        } else {
            renderStatePlaylists(PlaylistState.Empty)
        }
    }
    private fun renderStatePlaylists(state: PlaylistState){
        playlistsStateLiveData.postValue(state)
    }

    fun trackInPlaylist(playlist: Playlist, track: Track): Boolean{
        return track.id in playlist.listIds
    }

    fun addTrackToPlaylist(playlist: Playlist, track: Track){
        viewModelScope.launch {
            runBlocking {
                playlistsInteractor.addTrackToPlaylist(playlist, track)
                playlistsInteractor.addTrack(track)
            }
            getPlaylists()
        }
    }

}
