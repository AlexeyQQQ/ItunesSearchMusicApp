package ru.easycode.intensive2itunessearch.add.presentation

import ru.easycode.intensive2itunessearch.add.data.AddTrackRepository
import ru.easycode.intensive2itunessearch.add.data.Playlist
import ru.easycode.intensive2itunessearch.core.presentation.AbstractViewModel
import ru.easycode.intensive2itunessearch.core.presentation.RunAsync
import ru.easycode.intensive2itunessearch.dashboard.data.Track
import ru.easycode.intensive2itunessearch.dashboard.presentation.adapter.DashboardUi
import ru.easycode.intensive2itunessearch.playlists.presentation.ClickPlaylist

class AddTrackToPlaylistViewModel(
    uiObservable: AddTrackUiObservable,
    private val repository: AddTrackRepository,
    runAsync: RunAsync,
) : AbstractViewModel<AddTrackToPlaylistUiState, AddTrackUiObservable>(
    runAsync, uiObservable
), ClickPlaylist {

    private lateinit var cachedTrack: Track

    fun init(track: DashboardUi) {
        cachedTrack = Track(
            track.id().toLong(),
            track.trackUrl(),
            track.coverUrl(),
            track.trackName(),
            track.artistName(),
        )

        runAsync({
            repository.playlists(track = cachedTrack)
        }) { list ->
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
        }) { list ->
            uiObservable.updateUiState(
                AddTrackToPlaylistUiState.Initial(list = list.map {
                    PlaylistUi(id = it.id, name = it.name)
                })
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