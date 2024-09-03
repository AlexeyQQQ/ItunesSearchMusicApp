package ru.easycode.intensive2itunessearch.core.presentation.exo_player

import ru.easycode.intensive2itunessearch.core.presentation.CurrentlyPlayingPlaylistObservable
import ru.easycode.intensive2itunessearch.core.presentation.CurrentlyShowingObservable
import ru.easycode.intensive2itunessearch.dashboard.presentation.adapter.DashboardUi

interface MediaPlayer {

    suspend fun startController()

    suspend fun play(track: DashboardUi)

    fun stop()

    fun release()

    fun isPlaying(): Boolean

    fun updateCurrentlyShowingObservable(currentlyShowingObservable: CurrentlyShowingObservable)
    fun clearCurrentlyShowingObservable() =
        updateCurrentlyShowingObservable(CurrentlyShowingObservable.Empty)

    fun updateCurrentlyPlayingObservable(currentlyPlayingPlaylistObservable: CurrentlyPlayingPlaylistObservable)
    fun clearCurrentlyPlayingObservable() =
        updateCurrentlyPlayingObservable(CurrentlyPlayingPlaylistObservable.Empty)

    class Base(
        private val mediaPlayerRepository: MediaPlayerRepository,
        private val controllerWrapper: ControllerWrapper,
    ) : MediaPlayer {

        private var currentlyShowingPlaylistObservable: CurrentlyShowingObservable =
            CurrentlyShowingObservable.Empty
        private var currentlyPlayingPlaylistObservable: CurrentlyPlayingPlaylistObservable =
            CurrentlyPlayingPlaylistObservable.Empty

        override suspend fun startController() {
            controllerWrapper.startController()

            val listTracks = mediaPlayerRepository.updatePlaylistInController(controllerWrapper)
            currentlyPlayingPlaylistObservable.updateList(listTracks)
            controllerWrapper.initUpdateMediaItems(mediaPlayerRepository.cachedPlaylist())

            val notifyPlayingTrackId: (Long) -> Unit = {
                mediaPlayerRepository.saveCurrentTrackId(it)
                currentlyShowingPlaylistObservable.notifyIsNowPlayingTrackId(it)
                currentlyPlayingPlaylistObservable.notifyIsNowPlayingTrackId(it)
            }
            val notifyStopPlaying: () -> Unit = {
                currentlyShowingPlaylistObservable.notifyStopPlaying()
                currentlyPlayingPlaylistObservable.notifyStopPlaying()
            }
            controllerWrapper.addListeners(notifyPlayingTrackId, notifyStopPlaying)
        }

        override suspend fun play(track: DashboardUi) {
            if (mediaPlayerRepository.cachedPlaylistId() != track.playlistId()) {
                val listTracks = mediaPlayerRepository.updatePlaylistInController(controllerWrapper)
                currentlyPlayingPlaylistObservable.updateList(listTracks)
                controllerWrapper.updateMediaItems(mediaPlayerRepository.cachedPlaylist())
            }

            mediaPlayerRepository.cachedPlaylist().find {
                it.trackId.toString() == track.id()
            }?.let {
                val position = mediaPlayerRepository.cachedPlaylist().indexOf(it)
                controllerWrapper.play(position)
            }
        }

        override fun stop() {
            controllerWrapper.stop()
        }

        override fun release() {
            controllerWrapper.release()
        }

        override fun isPlaying(): Boolean = controllerWrapper.isPlaying()

        override fun updateCurrentlyShowingObservable(currentlyShowingObservable: CurrentlyShowingObservable) {
            this.currentlyShowingPlaylistObservable = currentlyShowingObservable
            controllerWrapper.notifyTrackStatus({
                mediaPlayerRepository.saveCurrentTrackId(it)
                currentlyShowingObservable.notifyIsNowPlayingTrackId(it)
            }) {
                currentlyShowingObservable.notifyStopPlaying()
            }
        }

        override fun updateCurrentlyPlayingObservable(currentlyPlayingPlaylistObservable: CurrentlyPlayingPlaylistObservable) {
            this.currentlyPlayingPlaylistObservable = currentlyPlayingPlaylistObservable
            controllerWrapper.notifyTrackStatus({
                mediaPlayerRepository.saveCurrentTrackId(it)
                currentlyPlayingPlaylistObservable.notifyIsNowPlayingTrackId(it)
            }) {
                currentlyPlayingPlaylistObservable.notifyStopPlaying()
            }
        }
    }
}