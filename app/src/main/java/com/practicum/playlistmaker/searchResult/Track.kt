package com.practicum.playlistmaker.searchResult

import android.content.Context
import android.os.Parcelable
import android.util.TypedValue
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.Locale

@Parcelize
data class Track(
    @SerializedName("trackId") val id  : Int,
    val trackName : String?, //название трека
    val artistName : String?,   //имя исполнителя
    @SerializedName("trackTimeMillis") val trackTime : Long?, //продолжительность трека
    @SerializedName("artworkUrl100") val albumPoster : String?, //обложка альбома
    val collectionName : String?, //название альбома
    val releaseDate: String?, //год релиза трека
    val primaryGenreName: String?, //жанр трека
    val country: String?, //страна исполнителя
    val previewUrl : String? //ссылка на отрывок трека
) :Parcelable {

    fun getCoverArtwork() = albumPoster?.replaceAfterLast('/', "512x512bb.jpg")

    fun getYearDateRelease() = releaseDate?.substring(0, 4)
}

fun dpToPx(dp: Float, context: Context): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        context.resources.displayMetrics
    ).toInt()
}

fun timeConversion(time: Long?): String {
    if (time == null) return ""
    return SimpleDateFormat("mm:ss", Locale.getDefault()).format(time)
}
