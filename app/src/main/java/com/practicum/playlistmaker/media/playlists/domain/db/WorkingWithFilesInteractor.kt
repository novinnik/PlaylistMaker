package com.practicum.playlistmaker.media.playlists.domain.db

import android.net.Uri

interface WorkingWithFilesInteractor {
    fun saveFileImage(uri: Uri?): Uri?
}