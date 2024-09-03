package ru.easycode.intensive2itunessearch.core.presentation.exo_player

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C.AUDIO_CONTENT_TYPE_MUSIC
import androidx.media3.common.C.USAGE_MEDIA
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.util.EventLogger
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import ru.easycode.intensive2itunessearch.main.presentation.MainActivity

class PlaybackMediaService : MediaSessionService() {

    private lateinit var player: ExoPlayer
    private lateinit var mediaSession: MediaSession

    override fun onCreate() {
        super.onCreate()
        player = ExoPlayer.Builder(this)
            .setAudioAttributes(AudioAttributes.Builder().run {
                setUsage(USAGE_MEDIA)
                setContentType(AUDIO_CONTENT_TYPE_MUSIC)
                build()
            }, true)
            .build()

        player.addAnalyticsListener(EventLogger())

        mediaSession = MediaSession.Builder(this, player)
            .also { builder ->
                builder.setSessionActivity(
                    PendingIntent.getActivity(
                        this,
                        0,
                        Intent(this, MainActivity::class.java),
                        immutableFlag or PendingIntent.FLAG_UPDATE_CURRENT
                    )
                )
            }
            .build()
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        val player = mediaSession.player
        if (!player.playWhenReady
            || player.mediaItemCount == 0
            || player.playbackState == Player.STATE_ENDED
        ) stopSelf()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return mediaSession
    }

    override fun onDestroy() {
        mediaSession.release()
        mediaSession.player.release()
        super.onDestroy()
    }

    companion object {
        @SuppressLint("ObsoleteSdkInt")
        private val immutableFlag =
            if (Build.VERSION.SDK_INT >= 23) PendingIntent.FLAG_IMMUTABLE else 0
    }
}