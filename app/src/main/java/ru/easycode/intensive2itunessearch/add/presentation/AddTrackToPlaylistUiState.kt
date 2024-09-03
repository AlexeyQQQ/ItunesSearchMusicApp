package ru.easycode.intensive2itunessearch.add.presentation

import android.view.View
import ru.easycode.intensive2itunessearch.core.presentation.views.UpdateButton
import ru.easycode.intensive2itunessearch.core.presentation.views.UpdateInput

interface AddTrackToPlaylistUiState {

    fun update(
        input: UpdateInput,
        createButton: UpdateButton,
        cancelButton: UpdateButton,
        saveButton: UpdateButton,
    ) = Unit

    fun update(updateList: UpdateList) = Unit

    fun closeFragment(function: () -> Unit) = Unit

    data class Initial(
        private val list: List<PlaylistUi>
    ) : AddTrackToPlaylistUiState {

        override fun update(
            input: UpdateInput,
            createButton: UpdateButton,
            cancelButton: UpdateButton,
            saveButton: UpdateButton
        ) {
            input.changeVisibility(View.GONE)
            createButton.changeVisibility(View.VISIBLE)
            cancelButton.changeVisibility(View.GONE)
            saveButton.changeVisibility(View.GONE)
        }

        override fun update(updateList: UpdateList) {
            updateList.update(list)
        }
    }

    object EmptyInput : AddTrackToPlaylistUiState {

        override fun update(
            input: UpdateInput,
            createButton: UpdateButton,
            cancelButton: UpdateButton,
            saveButton: UpdateButton
        ) {
            input.changeVisibility(View.VISIBLE)
            input.clearText()
            createButton.changeVisibility(View.GONE)
            cancelButton.changeVisibility(View.VISIBLE)
            saveButton.changeVisibility(View.VISIBLE)
            saveButton.changeEnabled(false)
        }
    }

    object NotEmptyInput : AddTrackToPlaylistUiState {

        override fun update(
            input: UpdateInput,
            createButton: UpdateButton,
            cancelButton: UpdateButton,
            saveButton: UpdateButton
        ) {
            input.changeVisibility(View.VISIBLE)
            createButton.changeVisibility(View.GONE)
            cancelButton.changeVisibility(View.VISIBLE)
            saveButton.changeVisibility(View.VISIBLE)
            saveButton.changeEnabled(true)
        }
    }

    object ListState : AddTrackToPlaylistUiState {
        override fun update(
            input: UpdateInput,
            createButton: UpdateButton,
            cancelButton: UpdateButton,
            saveButton: UpdateButton
        ) {
            input.changeVisibility(View.GONE)
            createButton.changeVisibility(View.VISIBLE)
            cancelButton.changeVisibility(View.GONE)
            saveButton.changeVisibility(View.GONE)
        }
    }

    object Close : AddTrackToPlaylistUiState {

        override fun closeFragment(function: () -> Unit) {
            function.invoke()
        }
    }
}