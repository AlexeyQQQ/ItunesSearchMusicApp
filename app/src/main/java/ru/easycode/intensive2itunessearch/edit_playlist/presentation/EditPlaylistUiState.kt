package ru.easycode.intensive2itunessearch.edit_playlist.presentation

import ru.easycode.intensive2itunessearch.core.presentation.views.UpdateButton

interface EditPlaylistUiState {

    fun update(renameButton: UpdateButton) = Unit

    fun closeFragment(function: () -> Unit) = Unit

    object Close : EditPlaylistUiState {
        override fun closeFragment(function: () -> Unit) {
            function.invoke()
        }
    }

    object SameValue : EditPlaylistUiState {
        override fun update(renameButton: UpdateButton) {
            renameButton.changeEnabled(false)
        }
    }

    object NotSameValue : EditPlaylistUiState {
        override fun update(renameButton: UpdateButton) {
            renameButton.changeEnabled(true)
        }
    }
}