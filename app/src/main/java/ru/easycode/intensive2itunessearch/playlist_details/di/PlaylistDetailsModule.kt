package ru.easycode.intensive2itunessearch.playlist_details.di

import androidx.lifecycle.ViewModel
import ru.easycode.intensive2itunessearch.core.di.Core
import ru.easycode.intensive2itunessearch.core.di.Module
import ru.easycode.intensive2itunessearch.core.di.ProvideAbstract
import ru.easycode.intensive2itunessearch.core.di.ProvideViewModel
import ru.easycode.intensive2itunessearch.dashboard.presentation.DashboardUiObservable
import ru.easycode.intensive2itunessearch.playlist_details.data.PlaylistDetailsRepository
import ru.easycode.intensive2itunessearch.playlist_details.presentation.PlaylistDetailsViewModel

class PlaylistDetailsModule(
    private val core: Core,
) : Module<PlaylistDetailsViewModel> {

    override fun viewModel(): PlaylistDetailsViewModel {
        return PlaylistDetailsViewModel(
            core.mediaPlayer,
            PlaylistDetailsRepository.Base(
                core.cacheDashboardModule.database().relationDao(),
                core.currentTrackIdCache,
                core.currentPlaylistCache,
            ),
            core.mainObservable,
            DashboardUiObservable.Base(),
            core.runAsync,
        )
    }
}

class ProvidePlaylistDetailsViewModel(
    private val core: Core,
    provideOther: ProvideViewModel
) : ProvideAbstract(provideOther, PlaylistDetailsViewModel::class.java) {

    override fun module(): Module<out ViewModel> = PlaylistDetailsModule(core)
}