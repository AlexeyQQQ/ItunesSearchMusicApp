package ru.easycode.intensive2itunessearch.core.presentation

import ru.easycode.intensive2itunessearch.core.presentation.exo_player.MediaPlayer
import ru.easycode.intensive2itunessearch.dashboard.presentation.DashboardUiObservable
import ru.easycode.intensive2itunessearch.dashboard.presentation.adapter.DashboardUi

abstract class AbstractActionsViewModel(
    private val mediaPlayer: MediaPlayer,
    private val repository: PlayTrackRepository,
    runAsync: RunAsync,
    uiObservable: DashboardUiObservable,
) : AbstractViewModel<List<DashboardUi>, DashboardUiObservable>(
    runAsync,
    uiObservable
), PlayerClickActions {

    override fun play(track: DashboardUi) {
        repository.saveCurrentPlayingPlaylist(track.playlistId())
        repository.saveCurrentlyPlayingId(track.id().toLong())

        launchUi { mediaPlayer.play(track) }
    }

    override fun stop() {
        mediaPlayer.stop()
    }
}


interface ClickActions : TrackDetails, PlayerClickActions

interface TrackDetails {
    fun openTrackDetails(track: DashboardUi)
}

interface PlayerClickActions {

    fun retry() = Unit

    fun play(track: DashboardUi)

    fun stop()
}