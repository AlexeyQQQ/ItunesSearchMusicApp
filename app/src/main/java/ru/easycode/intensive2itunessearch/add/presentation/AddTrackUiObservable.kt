package ru.easycode.intensive2itunessearch.add.presentation

import ru.easycode.intensive2itunessearch.core.presentation.UiObservable

interface AddTrackUiObservable : UiObservable<AddTrackToPlaylistUiState> {
    class Base : UiObservable.Abstract<AddTrackToPlaylistUiState>(), AddTrackUiObservable
}