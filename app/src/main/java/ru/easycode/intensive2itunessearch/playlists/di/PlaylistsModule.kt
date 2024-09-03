package ru.easycode.intensive2itunessearch.playlists.di

import ru.easycode.intensive2itunessearch.core.di.Core
import ru.easycode.intensive2itunessearch.core.di.Module
import ru.easycode.intensive2itunessearch.core.di.ProvideAbstract
import ru.easycode.intensive2itunessearch.core.di.ProvideViewModel
import ru.easycode.intensive2itunessearch.playlists.data.PlaylistsRepository
import ru.easycode.intensive2itunessearch.playlists.presentation.PlaylistsUiObservable
import ru.easycode.intensive2itunessearch.playlists.presentation.PlaylistsViewModel

class PlaylistsModule(
    private val core: Core,
) : Module<PlaylistsViewModel> {

    override fun viewModel(): PlaylistsViewModel {
        return PlaylistsViewModel(
            PlaylistsRepository.Base(
                core.cacheDashboardModule.database().playlistDao()
            ),
            core.runAsync,
            PlaylistsUiObservable.Base()
        )
    }

}

class ProvidePlaylistsViewModel(
    private val core: Core,
    provideOther: ProvideViewModel
) : ProvideAbstract(provideOther, PlaylistsViewModel::class.java) {
    override fun module() = PlaylistsModule(core)
}