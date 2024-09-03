package ru.easycode.intensive2itunessearch.playlists.presentation

import ru.easycode.intensive2itunessearch.add.presentation.PlaylistUi
import ru.easycode.intensive2itunessearch.core.presentation.AbstractViewModel
import ru.easycode.intensive2itunessearch.core.presentation.RunAsync
import ru.easycode.intensive2itunessearch.playlists.data.PlaylistsRepository

class PlaylistsViewModel(
    private val repository: PlaylistsRepository,
    runAsync: RunAsync,
    uiObservable: PlaylistsUiObservable,
) : AbstractViewModel<PlaylistsUiState, PlaylistsUiObservable>(runAsync, uiObservable),
    ClickPlaylist {

    fun init() {
        runAsync({
            repository.playlists().map { playlist ->
                PlaylistUi(id = playlist.id, name = playlist.name)
            }
        }) { listUi ->
            uiObservable.updateUiState(
                uiState = PlaylistsUiState.Base(listUi)
            )
        }
    }

    override fun clickPlaylist(playlist: PlaylistUi) {
        uiObservable.updateUiState(PlaylistsUiState.NavigateToPlaylistDetails(playlist))
    }
}

interface PlaylistClickActions : ClickPlaylist, DeletePlaylist

interface ClickPlaylist {

    fun clickPlaylist(playlist: PlaylistUi)
}

interface DeletePlaylist {

    fun deletePlaylist(playlistUi: PlaylistUi)
}