package ru.easycode.intensive2itunessearch.core

import android.app.Application
import androidx.lifecycle.ViewModel
import ru.easycode.intensive2itunessearch.core.di.Core
import ru.easycode.intensive2itunessearch.core.di.ManageViewModels
import ru.easycode.intensive2itunessearch.core.di.ProvideViewModel
import ru.easycode.intensive2itunessearch.core.presentation.PicEngine
import ru.easycode.intensive2itunessearch.core.presentation.views.ProvidePicEngine

class App : Application(), ManageViewModels, ProvidePicEngine {

    private lateinit var factory: ManageViewModels
    private lateinit var core: Core

    override fun onCreate() {
        super.onCreate()
        core = Core(this)
        factory = ProvideViewModel.Factory(ProvideViewModel.Make(core))
    }

    override fun clear(clazz: Class<out ViewModel>) {
        factory.clear(clazz)
    }

    override fun <T : ViewModel> viewModel(clazz: Class<T>): T {
        return factory.viewModel(clazz)
    }

    override fun engine(): PicEngine {
        return if (core.runUiTest) PicEngine.Mock() else PicEngine.Base()
    }

}