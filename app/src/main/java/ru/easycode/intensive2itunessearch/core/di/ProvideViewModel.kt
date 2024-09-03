package ru.easycode.intensive2itunessearch.core.di

import androidx.lifecycle.ViewModel
import ru.easycode.intensive2itunessearch.add.di.ProvideAddTrackToPlaylistViewModel
import ru.easycode.intensive2itunessearch.create_playlist.di.ProvideCreatePlaylistsViewModel
import ru.easycode.intensive2itunessearch.dashboard.di.ProvideDashboardViewModel
import ru.easycode.intensive2itunessearch.delete_playlist.di.ProvideDeletePlaylistsViewModel
import ru.easycode.intensive2itunessearch.main.di.ProvideMainViewModel
import ru.easycode.intensive2itunessearch.playlist_details.di.ProvidePlaylistDetailsViewModel
import ru.easycode.intensive2itunessearch.playlists.di.ProvidePlaylistsViewModel
import ru.easycode.intensive2itunessearch.track_actions.di.ProvideTrackActionsInPlaylistViewModel

interface ProvideViewModel {

    fun <T : ViewModel> viewModel(clazz: Class<T>): T

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val make: ProvideViewModel
    ) : ManageViewModels {

        private val mutableMap = mutableMapOf<Class<out ViewModel>, ViewModel?>()

        override fun clear(clazz: Class<out ViewModel>) {
            mutableMap[clazz] = null
        }

        override fun <T : ViewModel> viewModel(clazz: Class<T>): T {
            return if (mutableMap[clazz] == null) {
                val viewModel = make.viewModel(clazz)
                mutableMap[clazz] = viewModel
                viewModel
            } else
                mutableMap[clazz] as T
        }
    }

    class Make(core: Core) : ProvideViewModel {

        private val chain: ProvideViewModel

        init {
            var temp: ProvideViewModel = ErrorProvideViewModel()
            temp = ProvidePlaylistsViewModel(core, temp)
            temp = ProvidePlaylistDetailsViewModel(core, temp)
            temp = ProvideAddTrackToPlaylistViewModel(core, temp)
            temp = ProvideTrackActionsInPlaylistViewModel(core, temp)
            temp = ProvideCreatePlaylistsViewModel(core, temp)
            temp = ProvideDeletePlaylistsViewModel(core, temp)
            temp = ProvideDashboardViewModel(core, temp)
            chain = ProvideMainViewModel(core, temp)
        }

        override fun <T : ViewModel> viewModel(clazz: Class<T>): T {
            return chain.viewModel(clazz)
        }
    }
}