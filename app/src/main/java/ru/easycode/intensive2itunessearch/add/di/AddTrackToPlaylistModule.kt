package ru.easycode.intensive2itunessearch.add.di

import ru.easycode.intensive2itunessearch.add.data.AddTrackRepository
import ru.easycode.intensive2itunessearch.add.presentation.AddTrackToPlaylistViewModel
import ru.easycode.intensive2itunessearch.add.presentation.AddTrackUiObservable
import ru.easycode.intensive2itunessearch.core.data.cache.Now
import ru.easycode.intensive2itunessearch.core.di.Core
import ru.easycode.intensive2itunessearch.core.di.Module
import ru.easycode.intensive2itunessearch.core.di.ProvideAbstract
import ru.easycode.intensive2itunessearch.core.di.ProvideViewModel

class AddTrackToPlaylistModule(
    private val core: Core,
) : Module<AddTrackToPlaylistViewModel> {

    override fun viewModel(): AddTrackToPlaylistViewModel {
        val db = core.cacheDashboardModule.database()

        return AddTrackToPlaylistViewModel(
            AddTrackUiObservable.Base(),
            AddTrackRepository.Base(
                Now.Base(),
                db.playlistDao(),
                db.relationDao(),
            ),
            core.runAsync,
        )
    }
}

class ProvideAddTrackToPlaylistViewModel(
    private val core: Core,
    provideOther: ProvideViewModel
) : ProvideAbstract(provideOther, AddTrackToPlaylistViewModel::class.java) {

    override fun module() = AddTrackToPlaylistModule(core)
}