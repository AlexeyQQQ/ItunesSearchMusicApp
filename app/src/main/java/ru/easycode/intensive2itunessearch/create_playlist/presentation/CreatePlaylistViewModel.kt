package ru.easycode.intensive2itunessearch.create_playlist.presentation

import ru.easycode.intensive2itunessearch.core.presentation.AbstractViewModel
import ru.easycode.intensive2itunessearch.core.presentation.RunAsync
import ru.easycode.intensive2itunessearch.create_playlist.data.CreatePlaylistRepository

class CreatePlaylistViewModel(
    private val repository: CreatePlaylistRepository,
    uiObservable: CreatePlaylistUiObservable,
    runAsync: RunAsync,
) : AbstractViewModel<CreatePlaylistUiState, CreatePlaylistUiObservable>(
    runAsync,
    uiObservable
) {

    fun checkUserInput(text: String) {
        uiObservable.updateUiState(
            if (text.isEmpty()) CreatePlaylistUiState.EmptyInput
            else CreatePlaylistUiState.NotEmptyInput
        )
    }

    fun savePlaylist(playlistName: String) {
        runAsync({ repository.createPlaylist(playlistName) }) {
            uiObservable.updateUiState(CreatePlaylistUiState.Close)
        }
    }
}

