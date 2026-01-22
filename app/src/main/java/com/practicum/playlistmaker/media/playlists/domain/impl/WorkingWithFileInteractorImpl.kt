package com.practicum.playlistmaker.media.playlists.domain.impl

import android.net.Uri
import com.practicum.playlistmaker.media.playlists.domain.db.WorkingWithFilesInteractor
import com.practicum.playlistmaker.media.playlists.domain.db.WorkingWithFilesRepository

class WorkingWithFileInteractorImpl(
    private val workingWithFilesRepository: WorkingWithFilesRepository): WorkingWithFilesInteractor {
    override fun saveFileImage(uri: Uri?): Uri? {
        return workingWithFilesRepository.saveFileImage(uri)
    }

}