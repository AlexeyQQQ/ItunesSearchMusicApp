package ru.easycode.intensive2itunessearch.playlists.presentation

import androidx.fragment.app.Fragment
import ru.easycode.intensive2itunessearch.core.presentation.Screen

object PlaylistsScreen : Screen.Replace() {

    override fun fragment(): Fragment {
        return PlaylistsFragment()
    }
}