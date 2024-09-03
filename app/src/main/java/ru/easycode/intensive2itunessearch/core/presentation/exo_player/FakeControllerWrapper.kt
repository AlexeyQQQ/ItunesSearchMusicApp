package ru.easycode.intensive2itunessearch.core.presentation.exo_player

import android.os.Handler
import android.os.Looper
import ru.easycode.intensive2itunessearch.core.data.cache.TrackCache

class FakeControllerWrapper : ControllerWrapper {

    private val handler = Handler(Looper.getMainLooper())
    private var autoStopPlaying: () -> Unit = {
        isPlaying = false
        notifyStopPlaying.invoke()
        if (currentPosition != cacheList.size - 1) play(currentPosition + 1)
    }

    private var cacheList: List<TrackCache> = emptyList()
    private var currentTrackId: Long = 0
    private var currentPosition: Int = 0
    private var isPlaying: Boolean = false

    private lateinit var notifyPlayingTrackId: (Long) -> Unit
    private lateinit var notifyStopPlaying: () -> Unit

    override fun startController() = Unit

    override fun initUpdateMediaItems(list: List<TrackCache>) {
        if (cacheList.isEmpty()) cacheList = list
    }

    override fun updateMediaItems(list: List<TrackCache>) {
        cacheList = list
    }

    override fun addListeners(
        notifyPlayingTrackId: (Long) -> Unit,
        notifyStopPlaying: () -> Unit
    ) {
        this.notifyPlayingTrackId = notifyPlayingTrackId
        this.notifyStopPlaying = notifyStopPlaying
    }

    override fun play(position: Int) {
        isPlaying = true
        currentPosition = position
        currentTrackId = cacheList[position].trackId
        notifyPlayingTrackId.invoke(currentTrackId)
        handler.postDelayed(autoStopPlaying, 2000)
    }

    override fun stop() {
        handler.removeCallbacksAndMessages(null)
        isPlaying = false
        notifyStopPlaying.invoke()
    }

    override fun release() = Unit

    override fun notifyTrackStatus(
        notifyPlayingTrackId: (Long) -> Unit,
        notifyStopPlaying: () -> Unit
    ) {
        if (isPlaying) {
            notifyPlayingTrackId.invoke(currentTrackId)
        } else {
            notifyStopPlaying.invoke()
        }
    }

    override fun isPlaying(): Boolean = isPlaying
}