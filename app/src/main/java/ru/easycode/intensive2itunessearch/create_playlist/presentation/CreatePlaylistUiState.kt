package ru.easycode.intensive2itunessearch.create_playlist.presentation

import android.view.View
import ru.easycode.intensive2itunessearch.core.presentation.views.CustomInput
import ru.easycode.intensive2itunessearch.core.presentation.views.UpdateButton

interface CreatePlaylistUiState {

    fun updateUi(
        input: CustomInput,
        saveButton: UpdateButton,
        cancelButton: UpdateButton
    ) = Unit

    fun closeFragment(function: () -> Unit) = Unit

    object EmptyInput : CreatePlaylistUiState {
        override fun updateUi(
            input: CustomInput,
            saveButton: UpdateButton,
            cancelButton: UpdateButton
        ) {
            input.changeVisibility(View.VISIBLE)
            saveButton.changeVisibility(View.VISIBLE)
            cancelButton.changeVisibility(View.VISIBLE)
            saveButton.changeEnabled(false)
        }
    }

    object NotEmptyInput : CreatePlaylistUiState {
        override fun updateUi(
            input: CustomInput,
            saveButton: UpdateButton,
            cancelButton: UpdateButton
        ) {
            input.changeVisibility(View.VISIBLE)
            saveButton.changeVisibility(View.VISIBLE)
            cancelButton.changeVisibility(View.VISIBLE)
            saveButton.changeEnabled(true)
        }
    }

    object Close : CreatePlaylistUiState {
        override fun closeFragment(function: () -> Unit) {
            function.invoke()
        }
    }
}