package com.practicum.playlistmaker.media.playlists.ui.view_model

import androidx.lifecycle.ViewModel
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.media.playlists.domain.db.PlaylistsInteractor
import com.practicum.playlistmaker.media.playlists.domain.db.WorkingWithFilesInteractor
import com.practicum.playlistmaker.media.playlists.domain.model.Playlist
import com.practicum.playlistmaker.media.playlists.model.FormPlaylistState
import kotlinx.coroutines.launch

class PlaylistAddViewModel(
    private val playlistsInteractor: PlaylistsInteractor,
    private val workingWithFilesInteractor: WorkingWithFilesInteractor
) : ViewModel() {

    private val stateForm = MutableLiveData<FormPlaylistState>()
    val stateFormLiveData: LiveData<FormPlaylistState> = stateForm

    private val statePlaylist = MutableLiveData<Playlist>()
    val statePlaylistLiveData: LiveData<Playlist> = statePlaylist

    private var imageFileUri: Uri? = null
    private var title = ""
    private var description = ""
    private var currentId: Int = -1

    init {
        updateStateForm()
    }

    private fun updateStateForm() {
        stateForm.value = FormPlaylistState(imageFileUri, title, description)
    }

    fun onTitleChanged(value: String) {
        title = value
        updateStateForm()
    }

    fun onDescriptionChanged(value: String) {
        description = value
        updateStateForm()
    }

    fun onImageSelected(value: Uri?) {
        if (value != null) {
            imageFileUri = value
            updateStateForm()
        }
    }

    fun saveNewPlaylist() {
        viewModelScope.launch {
            val savePathFile = workingWithFilesInteractor.saveFileImage(imageFileUri, Uri.EMPTY)
            val playlist = Playlist(
                image = savePathFile,
                title = title,
                description = description,
                listIds = listOf(),
                count = 0
            )
            playlistsInteractor.addPlaylist(playlist)
        }
    }

    fun updatePlaylist() {
        viewModelScope.launch {
            val imagePlaylist = statePlaylistLiveData.getValue()?.image

            val savePathFile = workingWithFilesInteractor.saveFileImage(imageFileUri, imagePlaylist)

            val tracksIds = statePlaylistLiveData.getValue()?.listIds ?: listOf()

            val newPlaylist = Playlist(
                id = currentId ?: 0,
                image = savePathFile,
                title = title,
                description = description,
                listIds = tracksIds,
                count = tracksIds.count()
            )
            playlistsInteractor.updatePlaylist(newPlaylist)
        }
    }

    fun setDataPlaylist(id: Int) {
        currentId = id
        viewModelScope.launch {
            playlistsInteractor.getPlaylistById(id).collect { playlist ->
                statePlaylist.postValue(playlist)
                title = playlist.title
                description = playlist.description
                if (playlist.image != null) {
                    imageFileUri = playlist.image
                }
                updateStateForm()
            }
        }
    }

}