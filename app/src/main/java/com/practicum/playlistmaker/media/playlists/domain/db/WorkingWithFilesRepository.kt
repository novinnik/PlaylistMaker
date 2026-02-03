package com.practicum.playlistmaker.media.playlists.domain.db

import android.net.Uri

interface WorkingWithFilesRepository {
    fun saveFileImage(uri: Uri?, oldUri: Uri?): Uri
}