package ru.easycode.intensive2itunessearch.main.presentation

import ru.easycode.intensive2itunessearch.core.presentation.UiObservable

interface MainObservable : UiObservable<MainUiState> {
    class Base : UiObservable.Abstract<MainUiState>(), MainObservable
}