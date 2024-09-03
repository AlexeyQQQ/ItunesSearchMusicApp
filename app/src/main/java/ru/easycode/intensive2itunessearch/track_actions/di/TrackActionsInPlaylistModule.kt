package ru.easycode.intensive2itunessearch.track_actions.di

import androidx.lifecycle.ViewModel
import ru.easycode.intensive2itunessearch.add.presentation.AddTrackUiObservable
import ru.easycode.intensive2itunessearch.core.data.cache.Now
import ru.easycode.intensive2itunessearch.core.di.Core
import ru.easycode.intensive2itunessearch.core.di.Module
import ru.easycode.intensive2itunessearch.core.di.ProvideAbstract
import ru.easycode.intensive2itunessearch.core.di.ProvideViewModel
import ru.easycode.intensive2itunessearch.track_actions.data.TrackActionsRepository
import ru.easycode.intensive2itunessearch.track_actions.presentation.TrackActionsInPlaylistViewModel

class TrackActionsInPlaylistModule(
    private val core: Core,
) : Module<TrackActionsInPlaylistViewModel> {

    override fun viewModel(): TrackActionsInPlaylistViewModel {
        val db = core.cacheDashboardModule.database()

        return TrackActionsInPlaylistViewModel(
            AddTrackUiObservable.Base(),
            TrackActionsRepository.Base(
                Now.Base(),
                db.playlistDao(),
                db.relationDao(),
            ),
            core.runAsync
        )
    }
}

class ProvideTrackActionsInPlaylistViewModel(
    private val core: Core,
    provideOther: ProvideViewModel
) : ProvideAbstract(provideOther, TrackActionsInPlaylistViewModel::class.java) {

    override fun module(): Module<out ViewModel> = TrackActionsInPlaylistModule(core)
}