package ru.easycode.intensive2itunessearch.dashboard.presentation

import ru.easycode.intensive2itunessearch.core.presentation.AbstractActionsViewModel
import ru.easycode.intensive2itunessearch.core.presentation.CurrentlyShowingObservable
import ru.easycode.intensive2itunessearch.core.presentation.RunAsync
import ru.easycode.intensive2itunessearch.core.presentation.exo_player.MediaPlayer
import ru.easycode.intensive2itunessearch.dashboard.data.DashboardRepository
import ru.easycode.intensive2itunessearch.dashboard.presentation.adapter.DashboardUi

class DashboardViewModel(
    private val mediaPlayer: MediaPlayer,
    uiObservable: DashboardUiObservable,
    private val repository: DashboardRepository,
    runAsync: RunAsync,
) : AbstractActionsViewModel(
    mediaPlayer,
    repository,
    runAsync,
    uiObservable
), CurrentlyShowingObservable {

    private var cachedQuery: String = ""
    private var cachedTracks: List<DashboardUi> = emptyList()

    fun readUserQuery(): String = repository.readUserQuery()

    fun search(query: String) {
        cachedQuery = query
        repository.saveUserQuery(query)
        uiObservable.updateUiState(listOf(DashboardUi.Progress))

        runAsync({
            repository.tracks(query)
        }, { loadResult ->
            if (loadResult.isSuccessful()) {
                val currentTrackId = repository.readCurrentlyPlayingId()
                cachedTracks = loadResult.list().map {
                    DashboardUi.Track(
                        playlistId = loadResult.query(),
                        trackId = it.trackId,
                        trackUrl = it.trackUrl,
                        coverUrl = it.coverUrl,
                        trackName = it.trackName,
                        artistName = it.artistName,
                        playing = currentTrackId == it.trackId && mediaPlayer.isPlaying()
                    )
                }
                uiObservable.updateUiState(cachedTracks)
            } else {
                uiObservable.updateUiState(
                    listOf(DashboardUi.Error(loadResult.message()))
                )
            }
        })
    }

    override fun retry() {
        search(cachedQuery)
    }

    override fun notifyIsNowPlayingTrackId(trackId: Long) {
        cachedTracks.find {
            it.id() == trackId.toString()
        }?.let {
            uiObservable.play(it)
        } ?: uiObservable.stop()
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

