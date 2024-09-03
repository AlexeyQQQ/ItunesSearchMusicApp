package ru.easycode.intensive2itunessearch.main.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import ru.easycode.intensive2itunessearch.core.presentation.CurrentlyPlayingPlaylistObservable
import ru.easycode.intensive2itunessearch.core.presentation.PlayerClickActions
import ru.easycode.intensive2itunessearch.core.presentation.RunAsync
import ru.easycode.intensive2itunessearch.core.presentation.UpdateUi
import ru.easycode.intensive2itunessearch.core.presentation.exo_player.MediaPlayer
import ru.easycode.intensive2itunessearch.dashboard.presentation.DashboardUiObservable
import ru.easycode.intensive2itunessearch.dashboard.presentation.adapter.DashboardUi
import ru.easycode.intensive2itunessearch.main.data.MainRepository

class MainViewModel(
    private val mainObservable: MainObservable,
    private val dashboardUiObservable: DashboardUiObservable,
    private val mediaPlayer: MediaPlayer,
    private val repository: MainRepository,
    private val runAsync: RunAsync,
) : ViewModel(), CurrentlyPlayingPlaylistObservable, PlayerClickActions {

    private var cachedTracks: List<DashboardUi> = emptyList()
    private var currentTrackIndex: Int = 0

    private val viewModelScope = CoroutineScope(
        SupervisorJob() + Dispatchers.Main.immediate
    )

    fun startUpdates(
        showUi: UpdateUi<MainUiState>,
        updateListDashboardUi: UpdateUi<List<DashboardUi>>
    ) {
        mainObservable.updateObserver(showUi)
        dashboardUiObservable.updateObserver(updateListDashboardUi)
        mediaPlayer.updateCurrentlyPlayingObservable(this)
    }

    fun stopUpdates() {
        mainObservable.clearObserver()
        dashboardUiObservable.clearObserver()
        mediaPlayer.clearCurrentlyPlayingObservable()
    }

    fun notifyObserved() {
        mainObservable.clear()
    }

    fun previousTrack() {
        if (currentTrackIndex == 0) return

        val previous = cachedTracks[currentTrackIndex - 1]
        if (mediaPlayer.isPlaying()) {
            play(previous)
        } else {
            currentTrackIndex -= 1
            mainObservable.updateUiState(
                MainUiState.ShowPlayer(
                    trackName = previous.trackName(),
                    artistName = previous.artistName(),
                    coverUrl = previous.coverUrl(),
                    isPlaying = false
                )
            )
        }
    }

    fun nextTrack() {
        if (currentTrackIndex == cachedTracks.size - 1) return

        val next = cachedTracks[currentTrackIndex + 1]
        if (mediaPlayer.isPlaying()) {
            play(next)
        } else {
            currentTrackIndex += 1
            mainObservable.updateUiState(
                MainUiState.ShowPlayer(
                    trackName = next.trackName(),
                    artistName = next.artistName(),
                    coverUrl = next.coverUrl(),
                    isPlaying = false
                )
            )
        }
    }

    fun playOrStop() {
        if (mediaPlayer.isPlaying()) {
            stop()
        } else {
            play(cachedTracks[currentTrackIndex])
        }
    }

    override fun play(track: DashboardUi) {
        currentTrackIndex = cachedTracks.indexOf(track)

        repository.saveCurrentPlayingPlaylist(track.playlistId())
        repository.saveCurrentlyPlayingId(track.id().toLong())

        runAsync.launchUi(viewModelScope) {
            mediaPlayer.play(track)
        }
    }

    override fun stop() {
        mediaPlayer.stop()
        cachedTracks.find {
            it.id() == repository.readCurrentlyPlayingId().toString()
        }?.let { currentTrackIndex = cachedTracks.indexOf(it) }
    }

    fun startController() {
        runAsync.launchUi(viewModelScope) {
            mediaPlayer.startController()
        }
    }

    fun releaseController() {
        mediaPlayer.release()
    }

    override fun updateList(list: List<DashboardUi>) {
        cachedTracks = list
        dashboardUiObservable.updateUiState(list)
    }

    override fun notifyIsNowPlayingTrackId(trackId: Long) {
        cachedTracks.find {
            it.id() == trackId.toString()
        }?.let {
            currentTrackIndex = cachedTracks.indexOf(it)
            dashboardUiObservable.play(it)
            mainObservable.updateUiState(
                MainUiState.ShowPlayer(
                    trackName = it.trackName(),
                    artistName = it.artistName(),
                    coverUrl = it.coverUrl(),
                    isPlaying = true
                )
            )
        } ?: dashboardUiObservable.stop()
    }

    override fun notifyStopPlaying() {
        dashboardUiObservable.stop()
        if (cachedTracks.isNotEmpty()) {
            cachedTracks.find {
                it.id() == repository.readCurrentlyPlayingId().toString()
            }?.let {
                currentTrackIndex = cachedTracks.indexOf(it)
                mainObservable.updateUiState(
                    MainUiState.ShowPlayer(
                        trackName = it.trackName(),
                        artistName = it.artistName(),
                        coverUrl = it.coverUrl(),
                        isPlaying = false
                    )
                )
            }
        } else {
            mainObservable.updateUiState(MainUiState.HidePlayer)
        }
    }
}