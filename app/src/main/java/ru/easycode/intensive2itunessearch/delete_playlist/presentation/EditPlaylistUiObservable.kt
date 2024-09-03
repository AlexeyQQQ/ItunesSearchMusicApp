package ru.easycode.intensive2itunessearch.delete_playlist.presentation

import ru.easycode.intensive2itunessearch.core.presentation.UiObservable


interface EditPlaylistUiObservable : UiObservable<EditPlaylistUiState> {

    class Base : UiObservable.Abstract<EditPlaylistUiState>(), EditPlaylistUiObservable
}