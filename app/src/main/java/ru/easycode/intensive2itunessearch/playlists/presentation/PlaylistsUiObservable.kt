package ru.easycode.intensive2itunessearch.playlists.presentation

import ru.easycode.intensive2itunessearch.core.presentation.UiObservable

interface PlaylistsUiObservable : UiObservable<PlaylistsUiState> {

    class Base : UiObservable.Abstract<PlaylistsUiState>(), PlaylistsUiObservable
}