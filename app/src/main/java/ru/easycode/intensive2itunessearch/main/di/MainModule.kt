package ru.easycode.intensive2itunessearch.main.di

import ru.easycode.intensive2itunessearch.core.di.Core
import ru.easycode.intensive2itunessearch.core.di.Module
import ru.easycode.intensive2itunessearch.core.di.ProvideAbstract
import ru.easycode.intensive2itunessearch.core.di.ProvideViewModel
import ru.easycode.intensive2itunessearch.dashboard.presentation.DashboardUiObservable
import ru.easycode.intensive2itunessearch.main.data.MainRepository
import ru.easycode.intensive2itunessearch.main.presentation.MainViewModel

class MainModule(
    private val core: Core,
) : Module<MainViewModel> {

    override fun viewModel(): MainViewModel {
        return MainViewModel(
            core.mainObservable,
            DashboardUiObservable.Base(),
            core.mediaPlayer,
            MainRepository.Base(
                core.currentTrackIdCache,
                core.currentPlaylistCache,
            ),
            core.runAsync,
        )
    }
}

class ProvideMainViewModel(
    private val core: Core,
    provideOther: ProvideViewModel
) : ProvideAbstract(provideOther, MainViewModel::class.java) {

    override fun module() = MainModule(core)
}