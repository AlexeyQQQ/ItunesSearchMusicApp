package ru.easycode.intensive2itunessearch.core.di

import androidx.lifecycle.ViewModel

@Suppress("UNCHECKED_CAST")
abstract class ProvideAbstract(
    private val nextChain: ProvideViewModel,
    private val viewModelClass: Class<out ViewModel>
) : ProvideViewModel {

    protected abstract fun module(): Module<out ViewModel>

    override fun <T : ViewModel> viewModel(clazz: Class<T>): T {
        return if (clazz == viewModelClass)
            module().viewModel() as T
        else
            nextChain.viewModel(clazz)
    }
}