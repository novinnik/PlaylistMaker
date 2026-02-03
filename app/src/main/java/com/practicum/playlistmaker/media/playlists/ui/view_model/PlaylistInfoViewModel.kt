package com.practicum.playlistmaker.media.playlists.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.media.playlists.domain.db.PlaylistsInteractor
import com.practicum.playlistmaker.media.playlists.model.PlaylistInfoState
import com.practicum.playlistmaker.setting.domain.api.ShareInteractor
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class PlaylistInfoViewModel(private val currentId:Int,
                            private val playlistsInteractor: PlaylistsInteractor,
                            private val shareInteractor: ShareInteractor): ViewModel() {

    private val stateLiveData = MutableLiveData<PlaylistInfoState>()
    fun observeState(): LiveData<PlaylistInfoState> = stateLiveData

    fun updateState(){
        viewModelScope.launch {
            playlistsInteractor.getPlaylistById(currentId).collect {
                    playlist ->
                val tracks = playlistsInteractor.getTracksInPlaylist(playlist)
                stateLiveData.postValue(PlaylistInfoState(playlist, tracks))
            }

        }
    }
    fun deletePlaylist(){
        viewModelScope.launch {
             playlistsInteractor.deletePlaylistById(currentId)
        }
    }
    fun deleteTrackFromPlaylist(idTrack: Int){
        viewModelScope.launch {
            runBlocking {
                playlistsInteractor.deleteTrackFromPlaylist(idTrack, currentId)
            }
            updateState()
        }
    }

    fun sharePlaylist(message: String){
        shareInteractor.shareApp(message)
    }
}