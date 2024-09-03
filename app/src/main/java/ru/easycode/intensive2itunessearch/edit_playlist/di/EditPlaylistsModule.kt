package ru.easycode.intensive2itunessearch.edit_playlist.di

import ru.easycode.intensive2itunessearch.core.di.Core
import ru.easycode.intensive2itunessearch.core.di.Module
import ru.easycode.intensive2itunessearch.core.di.ProvideAbstract
import ru.easycode.intensive2itunessearch.core.di.ProvideViewModel
import ru.easycode.intensive2itunessearch.edit_playlist.data.EditPlaylistRepository
import ru.easycode.intensive2itunessearch.edit_playlist.presentation.EditPlaylistUiObservable
import ru.easycode.intensive2itunessearch.edit_playlist.presentation.EditPlaylistViewModel

class EditPlaylistsModule(
    private val core: Core,
) : Module<EditPlaylistViewModel> {
    override fun viewModel(): EditPlaylistViewModel {
        val db = core.cacheDashboardModule.database()
        return EditPlaylistViewModel(
            repository = EditPlaylistRepository.Base(
                db.playlistDao(),
                db.relationDao(),
            ),
            uiObservable = EditPlaylistUiObservable.Base(),
            runAsync = core.runAsync
        )
    }
}

class ProvideDeletePlaylistsViewModel(
    private val core: Core,
    provideOther: ProvideViewModel
) : ProvideAbstract(provideOther, EditPlaylistViewModel::class.java) {
    override fun module() = EditPlaylistsModule(core)
}