package ru.easycode.intensive2itunessearch.create_playlist.di

import ru.easycode.intensive2itunessearch.core.data.cache.Now
import ru.easycode.intensive2itunessearch.core.di.Core
import ru.easycode.intensive2itunessearch.core.di.Module
import ru.easycode.intensive2itunessearch.core.di.ProvideAbstract
import ru.easycode.intensive2itunessearch.core.di.ProvideViewModel
import ru.easycode.intensive2itunessearch.create_playlist.data.CreatePlaylistRepository
import ru.easycode.intensive2itunessearch.create_playlist.presentation.CreatePlaylistUiObservable
import ru.easycode.intensive2itunessearch.create_playlist.presentation.CreatePlaylistViewModel

class CreatePlaylistsModule(
    private val core: Core,
) : Module<CreatePlaylistViewModel> {
    override fun viewModel(): CreatePlaylistViewModel {
        return CreatePlaylistViewModel(
            repository = CreatePlaylistRepository.Base(
                Now.Base(),
                core.cacheDashboardModule.database().playlistDao()
            ),
            uiObservable = CreatePlaylistUiObservable.Base(),
            runAsync = core.runAsync
        )
    }
}

class ProvideCreatePlaylistsViewModel(
    private val core: Core,
    provideOther: ProvideViewModel
) : ProvideAbstract(provideOther, CreatePlaylistViewModel::class.java) {
    override fun module() = CreatePlaylistsModule(core)
}