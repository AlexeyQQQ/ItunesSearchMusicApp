package ru.easycode.intensive2itunessearch.track_actions.presentation

import ru.easycode.intensive2itunessearch.add.data.Playlist
import ru.easycode.intensive2itunessearch.add.presentation.AddTrackToPlaylistUiState
import ru.easycode.intensive2itunessearch.add.presentation.AddTrackUiObservable
import ru.easycode.intensive2itunessearch.add.presentation.PlaylistUi
import ru.easycode.intensive2itunessearch.core.presentation.AbstractViewModel
import ru.easycode.intensive2itunessearch.core.presentation.RunAsync
import ru.easycode.intensive2itunessearch.dashboard.data.Track
import ru.easycode.intensive2itunessearch.dashboard.presentation.adapter.DashboardUi
import ru.easycode.intensive2itunessearch.playlists.presentation.ClickPlaylist
import ru.easycode.intensive2itunessearch.track_actions.data.TrackActionsRepository

class TrackActionsInPlaylistViewModel(
    uiObservable: AddTrackUiObservable,
    private val repository: TrackActionsRepository,
    runAsync: RunAsync,
) : AbstractViewModel<AddTrackToPlaylistUiState, AddTrackUiObservable>(
    runAsync, uiObservable
), ClickPlaylist {

    private lateinit var cachedTrack: Track
    private lateinit var cachedPlaylist: Playlist

    fun init(track: DashboardUi, playlist: PlaylistUi) {
        cachedTrack = Track(
            track.id().toLong(),
            track.trackUrl(),
            track.coverUrl(),
            track.trackName(),
            track.artistName(),
        )

        cachedPlaylist = Playlist(
            playlist.id,
            playlist.name
        )

        runAsync({
            repository.playlists(track = cachedTrack)
        }) { list: List<Playlist> ->
            uiObservable.updateUiState(
                AddTrackToPlaylistUiState.Initial(list = list.map {
                    PlaylistUi(id = it.id, name = it.name)
                })
            )
        }
    }

    fun createPlaylist() {
        uiObservable.updateUiState(AddTrackToPlaylistUiState.EmptyInput)
    }

    fun checkUserInput(text: String) {
        uiObservable.updateUiState(
            if (text.isEmpty()) AddTrackToPlaylistUiState.EmptyInput
            else AddTrackToPlaylistUiState.NotEmptyInput
        )
    }

    fun cancel() {
        uiObservable.updateUiState(AddTrackToPlaylistUiState.ListState)
    }

    fun savePlaylist(name: String) {
        runAsync({
            repository.savePlaylist(name)
            repository.playlists(track = cachedTrack)
        }) { list: List<Playlist> ->
            uiObservable.updateUiState(
                AddTrackToPlaylistUiState.Initial(list = list.map {
                    PlaylistUi(id = it.id, name = it.name)
                })
            )
        }
    }

    fun removeTrack() {
        runAsync({
            repository.removeTrackFromPlaylist(track = cachedTrack, playlist = cachedPlaylist)
        }) {
            uiObservable.updateUiState(
                AddTrackToPlaylistUiState.Close
            )
        }
    }

    override fun clickPlaylist(playlist: PlaylistUi) {
        runAsync({
            repository.addTrackToPlaylist(
                playlist = Playlist(playlist.id, playlist.name),
                track = cachedTrack,
            )
        }) {
            uiObservable.updateUiState(AddTrackToPlaylistUiState.Close)
        }
    }
}