package ru.easycode.intensive2itunessearch.delete_playlist.presentation

import ru.easycode.intensive2itunessearch.add.presentation.PlaylistUi
import ru.easycode.intensive2itunessearch.core.presentation.AbstractViewModel
import ru.easycode.intensive2itunessearch.core.presentation.RunAsync
import ru.easycode.intensive2itunessearch.delete_playlist.data.EditPlaylistRepository


class EditPlaylistViewModel(
    private val repository: EditPlaylistRepository,
    uiObservable: EditPlaylistUiObservable,
    runAsync: RunAsync
) : AbstractViewModel<EditPlaylistUiState, EditPlaylistUiObservable>(
    runAsync,
    uiObservable
) {

    private lateinit var cachedPlaylist: PlaylistUi

    fun init(playlist: PlaylistUi) {
        cachedPlaylist = playlist
    }

    fun deletePlaylist() {
        runAsync({ repository.deletePlaylist(cachedPlaylist.id) }) {
            uiObservable.updateUiState(EditPlaylistUiState.Close)
        }
    }

    fun checkUserInput(text: String) {
        uiObservable.updateUiState(
            if (text == cachedPlaylist.name) EditPlaylistUiState.SameValue
            else EditPlaylistUiState.NotSameValue
        )
    }

    fun renamePlaylist(name: String) {
        runAsync({ repository.renamePlaylist(cachedPlaylist.id, name) }) {
            uiObservable.updateUiState(EditPlaylistUiState.Close)
        }
    }
}