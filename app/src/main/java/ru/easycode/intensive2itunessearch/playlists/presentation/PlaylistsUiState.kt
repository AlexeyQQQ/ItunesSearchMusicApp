package ru.easycode.intensive2itunessearch.playlists.presentation

import ru.easycode.intensive2itunessearch.add.presentation.PlaylistUi
import ru.easycode.intensive2itunessearch.add.presentation.UpdateList

interface PlaylistsUiState {

    fun update(updateList: UpdateList)

    fun navigate(function: (PlaylistUi) -> Unit)

    data class Base (
        private val list: List<PlaylistUi>
    ) : PlaylistsUiState {

        override fun update(updateList: UpdateList) {
            updateList.update(list)
        }

        override fun navigate(function: (PlaylistUi) -> Unit) = Unit
    }

    data class NavigateToPlaylistDetails(private val playlistUi: PlaylistUi) : PlaylistsUiState {

        override fun update(updateList: UpdateList) = Unit

        override fun navigate(function: (PlaylistUi) -> Unit) {
            function.invoke(playlistUi)
        }
    }
}