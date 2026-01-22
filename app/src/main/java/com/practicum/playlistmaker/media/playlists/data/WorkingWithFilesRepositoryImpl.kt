package com.practicum.playlistmaker.media.playlists.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.core.net.toUri
import com.practicum.playlistmaker.media.playlists.domain.db.WorkingWithFilesRepository
import java.io.File
import java.io.FileOutputStream

class WorkingWithFilesRepositoryImpl(private val context: Context): WorkingWithFilesRepository {

    override fun saveFileImage(uri: Uri?): Uri? {

        if (uri == null) return null

        //создаем экземпляр класса File, который указывает на нужный каталог
        val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum_playlist")

        //создаем каталог, если он не создан
        if (!filePath.exists()){
            filePath.mkdirs()
        }

        //создадим имя файла добавив к нему время создания
        val fileName = "playlist_cover" + System.currentTimeMillis().toString() + ".jpg"

        //создаем экземпляр класса File, который указывает на файл внутри каталога
        val file = File(filePath, fileName)

        //создаем входящий потое байтов из выбранной картинки
        val inputStream = context.contentResolver.openInputStream(uri)

        //создаем исходящий поток байтов, в созданный выше файл
        val outputStream = FileOutputStream(file)

        //записываем картинку с помощью BitmapFactory
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)

        return file.toUri()
    }
}