package ru.easycode.intensive2itunessearch.dashboard.di

import ru.easycode.intensive2itunessearch.core.data.cache.Now
import ru.easycode.intensive2itunessearch.core.di.Core
import ru.easycode.intensive2itunessearch.core.di.Module
import ru.easycode.intensive2itunessearch.core.di.ProvideAbstract
import ru.easycode.intensive2itunessearch.core.di.ProvideViewModel
import ru.easycode.intensive2itunessearch.dashboard.data.DashboardRepository
import ru.easycode.intensive2itunessearch.dashboard.data.cache.CacheDashboardDataSource
import ru.easycode.intensive2itunessearch.dashboard.data.cloud.CloudDataSource
import ru.easycode.intensive2itunessearch.dashboard.data.cloud.FakeService
import ru.easycode.intensive2itunessearch.dashboard.presentation.DashboardUiObservable
import ru.easycode.intensive2itunessearch.dashboard.presentation.DashboardViewModel

class DashboardModule(
    private val core: Core,
) : Module<DashboardViewModel> {

    override fun viewModel(): DashboardViewModel {
        return DashboardViewModel(
            core.mediaPlayer,
            DashboardUiObservable.Base(),
            DashboardRepository.Base(
                if (core.runUiTest) CloudDataSource.Base(
                    2,
                    FakeService()
                ) else CloudDataSource.Base(50),
                CacheDashboardDataSource.Base(
                    core.cacheDashboardModule.database().trackDao(),
                    Now.Base(),
                ),
                core.currentTrackIdCache,
                core.currentPlaylistCache,
                core.userQuery,
            ),
            core.runAsync
        )
    }
}

class ProvideDashboardViewModel(
    private val core: Core,
    provideOther: ProvideViewModel
) : ProvideAbstract(provideOther, DashboardViewModel::class.java) {

    override fun module() = DashboardModule(core)
}