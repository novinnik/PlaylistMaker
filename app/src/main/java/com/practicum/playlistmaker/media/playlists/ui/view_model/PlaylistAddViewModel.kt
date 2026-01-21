package com.practicum.playlistmaker.media.playlists.ui.view_model

import androidx.lifecycle.ViewModel
import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.media.playlists.domain.db.PlaylistsInteractor
import com.practicum.playlistmaker.media.playlists.domain.db.WorkingWithFilesInteractor
import com.practicum.playlistmaker.media.playlists.domain.model.Playlist
import kotlinx.coroutines.launch

class PlaylistAddViewModel(
    private val playlistsInteractor: PlaylistsInteractor,
    private val workingWithFilesInteractor: WorkingWithFilesInteractor
): ViewModel() {

    fun saveNewPlaylist(uri: Uri?, title: String, description: String){
        val savePathFile = workingWithFilesInteractor.saveFileImage(uri)
        val newPlaylist = Playlist(
            image = savePathFile,
            title = title,
            description = description,
            listIds = listOf(),
            count = 0)
        savePlaylist(newPlaylist)
    }

    private fun savePlaylist(playlist: Playlist){
        viewModelScope.launch {
            playlistsInteractor
                .addPlaylist(playlist)
        }
    }

}