package com.practicum.playlistmaker

import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.appbar.MaterialToolbar
import com.google.gson.Gson
import com.practicum.playlistmaker.searchResult.*

class PlayerActivity : AppCompatActivity() {

    private lateinit var mediaPlayerLayout: ConstraintLayout

    private var player = MediaPlayer()

    private var timeProgress = 0L
    private var trackUrl : String? = null
    private var playerState = PlayerStatus.DEFAULT
    private var currentTrack: Track? = null

    private val playerHandler = Handler(Looper.getMainLooper())
    private var timerRunnable = Runnable{ setValueTimer() }

    private lateinit var backButtonToolbar: MaterialToolbar

    //описание и обложка трека
    private lateinit var imagePoster: ImageView
    private lateinit var trackName: TextView
    private lateinit var artistName: TextView

    //кнопки управления
    private lateinit var btnQueue: ImageButton
    private lateinit var btnPlay: ImageButton
    private lateinit var btnFavorite: ImageButton

    //время трека
    private lateinit var playTrackProgress: TextView

    //данные трека
    private lateinit var trackDurationValue: TextView
    private lateinit var trackCollectionValue: TextView
    private lateinit var trackYearValue: TextView
    private lateinit var trackGenreValue: TextView
    private lateinit var trackCountryValue: TextView

    //группы видимости
    private lateinit var groupCollection: Group
    private lateinit var groupYear: Group

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_player)

        initViews()

        backButtonToolbar.setNavigationOnClickListener { finish() }

        currentTrack = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(MEDIA_TRACK_KEY, Track::class.java)
        } else {
            @Suppress("DEPRECATION") // Для старых версий SDK
            intent.getParcelableExtra(MEDIA_TRACK_KEY)
        }

        currentTrack?.let { addMediaTrack(it) }

        playerPrepare()

        btnPlay.setOnClickListener{
            playerControl()
        }
    }

    private fun initViews() {
        backButtonToolbar = findViewById(R.id.toolbar_activity_player)

        mediaPlayerLayout = findViewById(R.id.constraint_layout_player)

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

    fun addMediaTrack(track: Track) {
        Glide.with(this)
            .load(track.getCoverArtwork())
            .placeholder(R.drawable.ic_placeholder)
            .fitCenter()
            .transform(RoundedCorners(dpToPx(CORNER_RADIUS, this)))
            .into(imagePoster)

        trackName.text = track.trackName
        artistName.text = track.artistName

        playTrackProgress.text = timeConversion(timeProgress)
        trackDurationValue.text = timeConversion(track.trackTime)

        if (track.collectionName.isNullOrEmpty()) {
            groupCollection.visibility = View.GONE
        } else {
            trackCollectionValue.text = track.collectionName
            groupCollection.visibility = View.VISIBLE
        }

        if (track.releaseDate.isNullOrEmpty()) {
            groupYear.visibility = View.GONE
        } else {
            trackYearValue.text = track.getYearDateRelease()
            groupYear.visibility = View.VISIBLE
        }

        trackGenreValue.text = track.primaryGenreName ?: ""
        trackCountryValue.text = track.country ?: ""

        trackUrl = track.previewUrl ?:""
    }

    override fun onPause() {
        super.onPause()
        playerPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
        playerHandler.removeCallbacksAndMessages(null)
    }

    private fun playerPrepare(){
        if (!trackUrl.isNullOrEmpty()){
            player.setDataSource(trackUrl)
            player.prepareAsync()
            player.setOnPreparedListener {
                playerState = PlayerStatus.PREPARED
            }
            player.setOnCompletionListener {
                playerState = PlayerStatus.PREPARED
                playerHandler.removeCallbacks(timerRunnable)
                changeImageButtonPlay()
                playTrackProgress.text = timeConversion(timeProgress)

            }
        }
    }

    private fun playerControl(){
        when(playerState){
            PlayerStatus.PLAY -> playerPause()
            PlayerStatus.PAUSE, PlayerStatus.PREPARED -> playerStart()
            PlayerStatus.DEFAULT -> {}
        }
    }

    private fun playerStart(){
        player.start()
        playerState = PlayerStatus.PLAY
        playerHandler.postDelayed(timerRunnable, PLAY_DELAY)
        changeImageButtonPlay()
    }

    private fun playerPause(){
        player.pause()
        playerState = PlayerStatus.PAUSE
        playerHandler.removeCallbacks(timerRunnable)
        changeImageButtonPlay()
    }

    private fun changeImageButtonPlay(){
        when(playerState){
            PlayerStatus.PLAY -> btnPlay.setImageResource(R.drawable.ic_pause)
            PlayerStatus.PAUSE, PlayerStatus.PREPARED -> btnPlay.setImageResource(R.drawable.ic_play)
            PlayerStatus.DEFAULT -> {btnPlay.setImageResource(R.drawable.ic_play)}
        }

    }

    private fun setValueTimer(){
        playTrackProgress.text = timeConversion(player.currentPosition.toLong())
        if (playerState == PlayerStatus.PLAY) {
            playerHandler.postDelayed(timerRunnable, PLAY_DELAY)
        }
    }

    companion object {
        private const val CORNER_RADIUS = 8f
        const val MEDIA_TRACK_KEY = "media_track_key"
        const val PLAY_DELAY = 500L
    }

    enum class PlayerStatus{
        //состоние плеера, на данном этапе изменение не требуется
        DEFAULT, //по умолчанию
        PREPARED,  //подготовка
        PLAY, //проигрывание
        PAUSE //пауза

    }

}