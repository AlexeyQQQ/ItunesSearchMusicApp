package ru.easycode.intensive2itunessearch.core.presentation.exo_player

import android.content.ComponentName
import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import ru.easycode.intensive2itunessearch.core.data.cache.TrackCache

interface ControllerWrapper : IsPlaying {

    fun startController()

    fun initUpdateMediaItems(list: List<TrackCache>)

    fun updateMediaItems(list: List<TrackCache>)

    fun addListeners(
        notifyPlayingTrackId: (Long) -> Unit,
        notifyStopPlaying: () -> Unit,
    )

    fun play(position: Int)

    fun stop()

    fun release()

    fun notifyTrackStatus(
        notifyPlayingTrackId: (Long) -> Unit,
        notifyStopPlaying: () -> Unit,
    )

    class Base(
        private val applicationContext: Context,
    ) : ControllerWrapper {

        private lateinit var controllerFuture: ListenableFuture<MediaController>
        private val controller: MediaController?
            get() = if (controllerFuture.isDone && !controllerFuture.isCancelled) controllerFuture.get() else null

        override fun startController() {
            controllerFuture = MediaController.Builder(
                applicationContext,
                SessionToken(
                    applicationContext,
                    ComponentName(applicationContext, PlaybackMediaService::class.java)
                )
            ).buildAsync()
        }

        override fun initUpdateMediaItems(list: List<TrackCache>) {
            controllerFuture.addListener({
                val controller = this.controller ?: return@addListener
                if (controller.mediaItemCount == 0) {
                    controller.setMediaItems(list.map {
                        it.mapToMediaItem()
                    })
                    controller.prepare()
                }
            }, MoreExecutors.directExecutor())
        }

        override fun updateMediaItems(list: List<TrackCache>) {
            controllerFuture.addListener({
                val controller = this.controller ?: return@addListener
                controller.setMediaItems(list.map {
                    it.mapToMediaItem()
                })
                controller.prepare()
            }, MoreExecutors.directExecutor())
        }

        override fun addListeners(
            notifyPlayingTrackId: (Long) -> Unit,
            notifyStopPlaying: () -> Unit,
        ) {
            controllerFuture.addListener({
                val controller = this.controller ?: return@addListener

                controller.addListener(
                    object : Player.Listener {
                        override fun onIsPlayingChanged(isPlaying: Boolean) {
                            super.onIsPlayingChanged(isPlaying)
                            if (isPlaying) {
                                val currentId =
                                    controller.getMediaItemAt(controller.currentMediaItemIndex).mediaId.toLong()
                                notifyPlayingTrackId.invoke(currentId)
                            } else {
                                notifyStopPlaying.invoke()
                            }
                        }

                        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                            super.onMediaItemTransition(mediaItem, reason)
                            if (controller.isPlaying && reason == Player.MEDIA_ITEM_TRANSITION_REASON_AUTO) {
                                val currentId =
                                    controller.getMediaItemAt(controller.currentMediaItemIndex).mediaId.toLong()
                                notifyPlayingTrackId.invoke(currentId)
                            }
                        }
                    }
                )

            }, MoreExecutors.directExecutor())
        }

        override fun play(position: Int) {
            val controller = this.controller ?: return
            if (position != controller.currentMediaItemIndex
                || position == controller.mediaItemCount - 1
            ) {
                controller.seekToDefaultPosition(position)
            }
            controller.playWhenReady = true
        }

        override fun stop() {
            controller?.playWhenReady = false
        }

        override fun release() {
            MediaController.releaseFuture(controllerFuture)
        }

        override fun isPlaying(): Boolean {
            return controller?.isPlaying ?: false
        }

        override fun notifyTrackStatus(
            notifyPlayingTrackId: (Long) -> Unit,
            notifyStopPlaying: () -> Unit,
        ) {
            controllerFuture.addListener({
                val controller = this.controller ?: return@addListener
                if (controller.isPlaying) {
                    val currentId =
                        controller.getMediaItemAt(controller.currentMediaItemIndex).mediaId.toLong()
                    notifyPlayingTrackId.invoke(currentId)
                } else {
                    notifyStopPlaying.invoke()
                }
            }, MoreExecutors.directExecutor())
        }
    }
}

interface IsPlaying {
    fun isPlaying(): Boolean
}