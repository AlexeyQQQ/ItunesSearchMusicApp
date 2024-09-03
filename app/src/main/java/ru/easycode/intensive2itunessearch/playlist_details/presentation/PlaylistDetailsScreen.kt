package ru.easycode.intensive2itunessearch.playlist_details.presentation

import androidx.fragment.app.Fragment
import ru.easycode.intensive2itunessearch.add.presentation.PlaylistUi
import ru.easycode.intensive2itunessearch.core.presentation.Screen

data class PlaylistDetailsScreen(private val playlistUi: PlaylistUi) :
    Screen.ReplaceWithBackstack() {

    override fun fragment(): Fragment {
        return PlaylistDetailsFragment.newInstance(playlistUi)
    }
}