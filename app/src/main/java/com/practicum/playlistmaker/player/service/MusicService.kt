package com.practicum.playlistmaker.player.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.player.ui.models.PlayerStatus
import com.practicum.playlistmaker.util.Converter.timeConversion
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class MusicService: Service(), AudioPlayerControl {

    private var player: MediaPlayer? = null
    private val binder = MusicServiceBinder()
    private var timerJob: Job? = null
    private val _playerState = MutableStateFlow<PlayerStatus>(PlayerStatus.Default())
    private var trackUrl = ""
    private var trackArtist = ""
    private var trackName = ""

    inner class MusicServiceBinder(): Binder(){
        fun getService(): MusicService = this@MusicService
    }

    override fun onBind(intent: Intent?): IBinder? {
        trackUrl = intent?.getStringExtra("track_url")?:""
        trackArtist = intent?.getStringExtra("track_artist")?:""
        trackName = intent?.getStringExtra("track_name")?:""

        initMediaPlayer()
        createNotificationChannel()

        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        playerRelease()
        return super.onUnbind(intent)
    }

    override fun onCreate() {
        super.onCreate()
        playerRelease()
        player = MediaPlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        playerRelease()
        stopSelf()
    }

    //region РАБОТА С ПЛЕЕРОМ
    private fun initMediaPlayer() {
        if (trackUrl.isEmpty()) return

        player?.setDataSource(trackUrl)
        player?.prepareAsync()
        player?.setOnPreparedListener {
            _playerState.value = PlayerStatus.Prepared()
        }
        player?.setOnCompletionListener {
            stopTimer()
            hideNotification()
            _playerState.value = PlayerStatus.Prepared()
        }
    }

    private fun playerRelease() {
       if (player == null) return

        stopTimer()
        player?.apply {
            stop()
            setOnPreparedListener(null)
            setOnCompletionListener(null)
            release()
        }
        player = null
        _playerState.value = PlayerStatus.Default()
    }

    override fun getPlayerState(): StateFlow<PlayerStatus> {
        return _playerState.asStateFlow()
    }

    override fun playerStart() {
        player?.start()
        _playerState.value = PlayerStatus.Play(getTimeProgress())
        startTimer()
    }

    override fun playerPause() {
       if (!isPlaying()) return

        player?.pause()
        stopTimer()
        _playerState.value = PlayerStatus.Pause(getTimeProgress())
    }

    private fun isPlaying(): Boolean {
        return player?.isPlaying == true
    }

    //endregion

    // region РАБОТА С ВРЕМЕНЕМ
    private fun startTimer() {
        timerJob = CoroutineScope(Dispatchers.Default).launch {
            while (isPlaying()){
                delay(PLAY_DELAY)
                _playerState.value = (PlayerStatus.Play(getTimeProgress()))
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    private fun getTimeProgress(): String {
        return timeConversion(player?.currentPosition?.toLong())
    }

    // endregion

    // region РАБОТА С СЕРВИСОМ И УВЕДОМЛЕНИЕМ
    private fun createNotificationChannel(){
        //создание сервиса
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            return
        }

        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        )
        channel.description = NOTIFICATION_CHANNEL_DESCRIPTION

        getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
    }

    private fun createServiceNotification(): Notification {
        val titleContent = getString(R.string.app_name)
        val textContent = if (trackArtist.isNotBlank() || trackName.isNotBlank()) {
            "$trackArtist - $trackName"
        } else {
            titleContent
        }
        //Генерируем поля и представления
        return NotificationCompat.Builder(this,NOTIFICATION_CHANNEL_ID)
            .setContentTitle(titleContent) // заголовок (первая строка)
            .setContentText(textContent) //описание (вторая строка)
            .setSmallIcon(R.mipmap.ic_launcher) //иконка маленькая
            .setPriority(NotificationCompat.PRIORITY_DEFAULT) //приоритет
            .setCategory(NotificationCompat.CATEGORY_SERVICE) //категория
            .setOngoing(true) //является ли уведомление текущим
            .setOnlyAlertOnce(true) //однократное уведомление
            .build() //объединяем все указанные параметры в один объект
    }

    private fun getForegroundServiceTypeConstant(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
        } else {
            0
        }
    }

    override fun showNotification() {
        if (!isPlaying()) return
        //запускаем сервис
        ServiceCompat.startForeground(
            this,
            NOTIFICATION_SERVICE_ID,
            createServiceNotification(),
            getForegroundServiceTypeConstant()
        )
    }

    override fun hideNotification() {
        //останавливаем сервис
        ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE)
    }
    // endregion

    companion object{
        private const val PLAY_DELAY = 200L
        private const val NOTIFICATION_SERVICE_ID = 123
        private const val NOTIFICATION_CHANNEL_ID = "music_service_channel"
        private const val NOTIFICATION_CHANNEL_NAME = "Music player service"
        private const val NOTIFICATION_CHANNEL_DESCRIPTION = "Service for playing music"

    }
}