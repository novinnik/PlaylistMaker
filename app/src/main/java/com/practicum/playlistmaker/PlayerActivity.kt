package com.practicum.playlistmaker

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.appbar.MaterialToolbar
import com.google.gson.Gson
import com.practicum.playlistmaker.searchResult.*

class PlayerActivity : AppCompatActivity() {

    private lateinit var mediaPlayer : ConstraintLayout
    private var playerStatus = STATUS_DEFAULT
    private var currentTrack : Track? = null
    private lateinit var backButtonToolbar : MaterialToolbar

    //описание и обложка трека
    private lateinit var imagePoster : ImageView
    private lateinit var trackName : TextView
    private lateinit var artistName : TextView

    //кнопки управления
    private lateinit var btnQueue : ImageButton
    private lateinit var btnPlay : ImageButton
    private lateinit var btnFavorite : ImageButton

    //время трека
    private lateinit var playTrackProgress : TextView

    //данные трека
    private lateinit var trackDurationValue : TextView
    private lateinit var trackCollectionValue : TextView
    private lateinit var trackYearValue : TextView
    private lateinit var trackGenreValue : TextView
    private lateinit var trackCountryValue : TextView

    //группы видимости
    private lateinit var groupCollection : Group
    private lateinit var groupYear : Group

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_player)

        initViews()

        backButtonToolbar.setNavigationOnClickListener { finish() }

        currentTrack = Gson().fromJson(intent.getStringExtra(MEDIA_TRACK_KEY), Track::class.java)

        currentTrack?.let { addMediaTrack(it) }

    }

    private fun initViews(){
        backButtonToolbar = findViewById(R.id.toolbar_activity_player)

        mediaPlayer = findViewById(R.id.constraint_layout_player)

        imagePoster = findViewById(R.id.play_image_poster)
        trackName = findViewById(R.id.play_track_name)
        artistName = findViewById(R.id.play_artist_name)

        btnQueue = findViewById(R.id.btn_queue)
        btnPlay = findViewById(R.id.btn_play)
        btnFavorite = findViewById(R.id.btn_favorite)

        playTrackProgress = findViewById(R.id.play_track_progress)

        trackDurationValue = findViewById(R.id.play_track_duration_value)
        trackCollectionValue = findViewById(R.id.play_track_collection_value)
        trackYearValue = findViewById(R.id.play_track_year_value)
        trackGenreValue = findViewById(R.id.play_track_genre_value)
        trackCountryValue = findViewById(R.id.play_track_country_value)

        groupCollection = findViewById(R.id.play_group_collection)
        groupYear = findViewById(R.id.play_group_year)
    }

    fun addMediaTrack(track: Track){
        Glide.with(this)
            .load(track.getCoverArtwork())
            .placeholder(R.drawable.ic_placeholder)
            .fitCenter()
            .transform(RoundedCorners(dpToPx(CORNER_RADIUS, this)))
            .into(imagePoster)

        trackName.text = track.trackName
        artistName.text = track.artistName

        playTrackProgress.text = timeConversion(0L)
        trackDurationValue.text = timeConversion(track.trackTime)

        if (track.collectionName.isNullOrEmpty()) {
            groupCollection.visibility = View.GONE
        } else {
            trackCollectionValue.text = track.collectionName
            groupCollection.visibility = View.VISIBLE
        }

        if (track.releaseDate.isNullOrEmpty()){
            groupYear.visibility = View.GONE
        } else {
            trackYearValue.text = track.getYearDateRelease()
            groupYear.visibility = View.VISIBLE
        }

        trackGenreValue.text = track.primaryGenreName ?: ""
        trackCountryValue.text = track.country ?: ""
    }

    companion object{
        //состоние плеера, на данном этапе изменение не требуется
        private const val STATUS_DEFAULT = 0 //по умолчанию
        private const val STATUS_PREPARED = 1 //подготовка
        private const val STATUS_PLAY = 2 //проигрывание
        private const val STATUS_PAUSE = 3 //пауза

        const val CORNER_RADIUS = 8f
        const val MEDIA_TRACK_KEY = "media_track_key"
    }

}