package ru.easycode.intensive2itunessearch.create_playlist.presentation

import ru.easycode.intensive2itunessearch.core.presentation.UiObservable

interface CreatePlaylistUiObservable : UiObservable<CreatePlaylistUiState> {

    class Base : UiObservable.Abstract<CreatePlaylistUiState>(), CreatePlaylistUiObservable

}
