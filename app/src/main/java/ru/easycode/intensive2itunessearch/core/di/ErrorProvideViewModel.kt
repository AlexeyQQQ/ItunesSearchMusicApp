package ru.easycode.intensive2itunessearch.core.di

import androidx.lifecycle.ViewModel

class ErrorProvideViewModel : ProvideViewModel {

    override fun <T : ViewModel> viewModel(clazz: Class<T>): T {
        throw IllegalStateException("unknown viewModel $clazz go and add it to ProvideViewModel.Make")
    }
}