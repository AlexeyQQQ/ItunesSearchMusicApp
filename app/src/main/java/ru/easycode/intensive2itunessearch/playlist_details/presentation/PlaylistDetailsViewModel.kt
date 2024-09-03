package ru.easycode.intensive2itunessearch.playlist_details.presentation

import ru.easycode.intensive2itunessearch.core.presentation.AbstractActionsViewModel
import ru.easycode.intensive2itunessearch.core.presentation.CurrentlyShowingObservable
import ru.easycode.intensive2itunessearch.core.presentation.RunAsync
import ru.easycode.intensive2itunessearch.core.presentation.exo_player.MediaPlayer
import ru.easycode.intensive2itunessearch.dashboard.presentation.DashboardUiObservable
import ru.easycode.intensive2itunessearch.dashboard.presentation.adapter.DashboardUi
import ru.easycode.intensive2itunessearch.main.presentation.MainObservable
import ru.easycode.intensive2itunessearch.main.presentation.MainUiState
import ru.easycode.intensive2itunessearch.playlist_details.data.PlaylistDetailsRepository

class PlaylistDetailsViewModel(
    private val mediaPlayer: MediaPlayer,
    private val repository: PlaylistDetailsRepository,
    private val mainObservable: MainObservable,
    uiObservable: DashboardUiObservable,
    runAsync: RunAsync,
) : AbstractActionsViewModel(
    mediaPlayer,
    repository,
    runAsync,
    uiObservable
), CurrentlyShowingObservable {

    private var cachedTracks: List<DashboardUi> = emptyList()

    fun init(playlistId: Long) {
        mainObservable.updateUiState(MainUiState.HideBottomNav)
        runAsync({
            repository.tracks(playlistId = playlistId)
        }, { list ->
            val currentTrackId = repository.readCurrentlyPlayingId()
            cachedTracks = list.map {
                DashboardUi.Track(
                    playlistId = playlistId.toString(),
                    trackId = it.trackId,
                    trackUrl = it.trackUrl,
                    coverUrl = it.coverUrl,
                    trackName = it.trackName,
                    artistName = it.artistName,
                    playing = currentTrackId == it.trackId && mediaPlayer.isPlaying()
                )
            }
            uiObservable.updateUiState(cachedTracks)
        })
    }

    fun showBottomNav() {
        mainObservable.updateUiState(MainUiState.ShowBottomNav)
    }

    override fun notifyIsNowPlayingTrackId(trackId: Long) {
        cachedTracks.find {
            it.id() == trackId.toString()
        }?.let {
            uiObservable.play(it)
        }
    }

    override fun notifyStopPlaying() {
        uiObservable.stop()
    }

    fun updateCurrentlyShowingObservable() {
        mediaPlayer.updateCurrentlyShowingObservable(this)
    }

    fun clearCurrentlyShowingObservable() {
        mediaPlayer.clearCurrentlyShowingObservable()
    }
}
