package com.practicum.playlistmaker.media.playlists.ui.view_model

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.media.playlists.domain.db.PlaylistsInteractor
import com.practicum.playlistmaker.media.playlists.domain.model.Playlist
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.setting.domain.api.ShareInteractor
import kotlinx.coroutines.launch

class PlaylistInfoViewModel(private val currentId:Int,
                            private val playlistsInteractor: PlaylistsInteractor,
                            private val shareInteractor: ShareInteractor): ViewModel() {

    private val statePlaylist = MutableLiveData<Playlist>()
    fun statePlaylistLiveData(): LiveData<Playlist> = statePlaylist
    private val stateTracks = MutableLiveData<List<Track>>()
    fun stateTracksLiveData(): LiveData<List<Track>> = stateTracks

    init {
        updateState()
    }

    private fun updateState(){
        viewModelScope.launch {
            playlistsInteractor.getPlaylistById(currentId).collect {
                    playlist ->
                statePlaylist.postValue(playlist)
                val tracks = playlistsInteractor.getTracksInPlaylist(playlist)
                stateTracks.postValue(tracks)
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
            playlistsInteractor.deleteTrackFromPlaylist(idTrack, currentId)
        }
        updateState()
    }

    fun sharePlaylist(message: String){
        shareInteractor.shareApp(message)
    }
}