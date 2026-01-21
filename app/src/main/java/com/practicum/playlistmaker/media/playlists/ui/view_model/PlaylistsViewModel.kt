package com.practicum.playlistmaker.media.playlists.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.media.playlists.domain.db.PlaylistsInteractor
import com.practicum.playlistmaker.media.playlists.domain.model.Playlist
import com.practicum.playlistmaker.media.playlists.model.PlaylistState
import kotlinx.coroutines.launch

class PlaylistsViewModel(
    private val playlistsInteractor: PlaylistsInteractor
): ViewModel() {

    private val stateLiveData = MutableLiveData< PlaylistState>(PlaylistState.Loading)
    fun observeState(): LiveData<PlaylistState> = stateLiveData

    fun fillData(){
        stateLiveData.postValue(PlaylistState.Loading)
        viewModelScope.launch {
            playlistsInteractor
                .getPlaylists()
                .collect { value ->
                    processResult(value)
                }
        }
    }

    private fun processResult(playlist: List<Playlist>){
        if (playlist.isNotEmpty()){
            renderState(PlaylistState.Content(playlist))
        } else {
            renderState(PlaylistState.Empty)
        }
    }
    private fun renderState(state: PlaylistState){
        stateLiveData.postValue(state)
    }
}